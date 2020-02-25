package example

package example

import cats.implicits._
import org.scalatest.FlatSpec
import cats.effect.IO
import scala.util.Random
import cats.effect.concurrent.Semaphore
import scala.concurrent.duration._

class RateLimitSpec extends FlatSpec {
  behavior of "rate limit"
  val ec = scala.concurrent.ExecutionContext.global

  implicit val cs = IO.contextShift(ec)
  implicit val timer = IO.timer(ec)
  it should "rate limit" in {
    val io: IO[Unit] =
      timer.sleep(2.seconds) >> IO(println(s"${java.time.Instant.now()}"))

    val p = for {
      sem <- Semaphore[IO](1)
      _ <- (1 until 5).toList
        .map(_ => RateLimit[IO, Unit](sem).apply(io))
        .parSequence
    } yield ()
    p.unsafeRunSync()
  }
}

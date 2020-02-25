package example

import org.scalatest.FlatSpec
import cats.effect.IO
import scala.util.Random

class RetrySpec extends FlatSpec {
  behavior of "retry"

  it should "retry" in {
    val fo: IO[Int] = {
      for {
        int <- IO(Random.nextInt(10))
        _ <- if (int > 3) IO.raiseError(new Exception("Ooops"))
        else IO.pure(int)
      } yield int
    }
    Retry[IO, Int](3).apply(fo).unsafeRunSync()
  }
}

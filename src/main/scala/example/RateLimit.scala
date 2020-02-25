package example

import cats.implicits._
import cats.effect.Sync
import cats.effect.concurrent.Semaphore

trait RateLimit[F[_], A] {

  /**
    *
    *
    * @param fa
    * @return
    */
  def apply(fa: F[A]): F[A]
}

object RateLimit {
//   private class RateLimitImpl[F[_]: Sync, A] extends RateLimit[F, A] {}
  def apply[F[_]: Sync, A](semaphore: Semaphore[F]): RateLimit[F, A] =
    new RateLimit[F, A] {
      def apply(fa: F[A]): F[A] =
        semaphore.withPermit(fa)
    }
}

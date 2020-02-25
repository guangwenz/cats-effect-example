package example

import cats.implicits._
import cats.effect.Sync

trait Retry[F[_], A] {

  /**
    * retry the effect F[A]
    *
    * @param fa
    * @return
    */
  def apply(fa: F[A]): F[A]
}

object Retry {
  def apply[F[_]: Sync, A](max: Int): Retry[F, A] = new Retry[F, A] {
    def apply(fa: F[A]): F[A] = fa.handleErrorWith {
      case ex =>
        if (max > 0) {
          Sync[F].pure(println("retrying")) >> Retry[F, A](max - 1).apply(fa)
        } else Sync[F].raiseError(ex)
    }
  }
}

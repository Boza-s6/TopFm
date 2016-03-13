package com.bozovic.nemanja;

public abstract class Either<T1, T2> {
    public static <T1, T2> Either<T1, T2> left(T1 t1) {
        return new Left<>(t1);
    }

    public static <T1, T2> Either<T1, T2> right(T2 t2) {
        return new Right<>(t2);
    }

    public abstract boolean isLeft();

    public abstract boolean isRight();

    public abstract T1 getLeft();

    public abstract T2 getRight();

    private static class Left<T1, T2> extends Either<T1, T2> {
        private T1 leftValue;

        public Left(T1 t) {
            leftValue = t;
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public T1 getLeft() {
            return leftValue;
        }

        @Override
        public T2 getRight() {
            throw new IllegalStateException("Use isLeft and isRight to check before pulling!");
        }
    }

    private static class Right<T1, T2> extends Either<T1, T2> {
        private T2 rightValue;

        public Right(T2 t) {
            rightValue = t;
        }

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public T1 getLeft() {
            throw new IllegalStateException("Use isLeft and isRight to check before pulling!");
        }

        @Override
        public T2 getRight() {
            return rightValue;
        }
    }
}

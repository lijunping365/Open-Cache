package com.saucesubfresh.cache.common.mutex;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author lijunping on 2022/9/1
 */
public class BooleanMutex {

    private static final class Sync extends AbstractQueuedSynchronizer {

        private static final long serialVersionUID = -5847885615350729062L;

        /** State value representing that TRUE */
        private static final int  TRUE             = 1;
        /** State value representing that FALSE */
        private static final int  FALSE            = 2;

        private boolean isTrue(int state) {
            return (state & TRUE) != 0;
        }

        /**
         * 实现AQS的接口，获取共享锁的判断
         */
        @Override
        protected int tryAcquireShared(int arg) {
            return super.tryAcquireShared(arg);
        }
    }
}

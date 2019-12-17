package zk;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class zkTest {
    /**
     * 集群地址
     */
    private static final String CONNECT_ADDRES = "localhost:2181";
    /**
     * 超时时间
     */
    private static final int SESSIONTIME = 2000;
    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zk=new ZooKeeper(CONNECT_ADDRES, SESSIONTIME, new Watcher() {
            /**
             * 事件触发的回调方法
             */
            public void process(WatchedEvent watchedEvent) {
                // 获取时间的状态
                Event.KeeperState keeperState = watchedEvent.getState();
                Event.EventType tventType = watchedEvent.getType();
                // 如果是建立连接
                if (Event.KeeperState.SyncConnected == keeperState) {
                    if (Event.EventType.None == tventType) {
                        // 如果建立连接成功,则发送信号量,让后阻塞程序向下执行
                        countDownLatch.countDown();
                        System.out.println("zk 建立连接");
                    }
                }
            }
        });
        // 进行阻塞
        countDownLatch.await();
        zk.delete("/testRott/children",-1);
        zk.delete("/testRott",-1);
        // 创建父节点
         String result = zk.create("/testRott", "12245465".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
         System.out.println("result:" + result);
        // 创建子节点
        String result2 = zk.create("/testRott/children", "children 12245465".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("result:" + result2);
        zk.delete("/testRott/children",-1);
        zk.delete("/testRott",-1);
        zk.close();

    }


}

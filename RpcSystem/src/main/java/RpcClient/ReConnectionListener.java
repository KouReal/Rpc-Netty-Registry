package RpcClient;


import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jsj
 * @date 2018-10-24
 */
public class ReConnectionListener implements ChannelFutureListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReConnectionListener.class);
    private Connection connection;

    public ReConnectionListener(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        Channel channel = channelFuture.channel();
        if (channelFuture.isSuccess()) {
            LOGGER.info("重连接成功: 新的channel:{}", channel);
            //重新绑定channel
            connection.bind(channel);
        } else {
            connection.addRetryCount();
            if (connection.getCount() < Connection.DEFAULT_RECONNECT_TRY) {
            	LOGGER.info("重连失败，正在进行第{}次重连", connection.getCount());
                ChannelPipeline channelPipeline = channel.pipeline();
                channelPipeline.fireChannelInactive();
            } else {
                LOGGER.info("重连失败，且已经达到最大重试次数:{},不再进行重试", Connection.DEFAULT_RECONNECT_TRY);
                connection.unbind();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

}

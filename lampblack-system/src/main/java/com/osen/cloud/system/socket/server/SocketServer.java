package com.osen.cloud.system.socket.server;

import com.osen.cloud.common.utils.ConstUtil;
import com.osen.cloud.system.socket.handler.SocketServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * User: PangYi
 * Date: 2019-09-04
 * Time: 14:54
 * Description: netty服务器端
 */
@Component
@Slf4j
public class SocketServer {

    /**
     * Socket服务器端
     */
    public void startServerSocket() {
        //负责处理客户端连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //负责处理网络请求
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    //服务端Socket
                    .channel(NioServerSocketChannel.class)
                    //初始化
                    .childHandler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ChannelPipeline channelPipeline = ch.pipeline();
                            //解决TCP粘包拆包的问题，以特定的字符结尾（）
                            channelPipeline.addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Unpooled.copiedBuffer("\r\n".getBytes())));
                            //字符串解码和编码
                            channelPipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
                            channelPipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                            channelPipeline.addLast(new IdleStateHandler(20, 0, 0, TimeUnit.SECONDS));
                            //服务器端逻辑处理
                            channelPipeline.addLast("SocketServerHandler", new SocketServerHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 512).childOption(ChannelOption.SO_KEEPALIVE, true);

            log.info("socket server starting port: " + ConstUtil.SERVER_PORT);

            //绑定端口，接受进来的连接
            ChannelFuture channelFuture = serverBootstrap.bind(ConstUtil.SERVER_PORT).sync();

            //等待服务器socket关闭
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            log.error(e.getMessage());
            log.error("socket server start error");
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            log.info("socket server closed");
        }
    }
}

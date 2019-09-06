package com.osen.cloud.system.socket.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * User: PangYi
 * Date: 2019-09-04
 * Time: 14:54
 * Description: 客户端连接处理
 */
@Slf4j
public class SocketServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 心跳丢失次数
     */
    private int counter = 0;

    /**
     * 处理读事件
     *
     * @param ctx 通道处理器
     * @param msg 消息
     * @throws Exception 异常
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("reading event start connection ID: " + getConnectionID(ctx));
        counter = 0;

        /*
            上传数据处理
         */

        System.out.println(msg.toString());
    }

    /**
     * 客户端注册连接
     *
     * @param ctx 通道处理器
     * @throws Exception 异常
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("client connection   ID: " + getConnectionID(ctx));
        ctx.fireChannelRegistered();
    }

    /**
     * 客户端断开连接
     *
     * @param ctx 通道处理器
     * @throws Exception 异常
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("client disconnection ID: " + getConnectionID(ctx));
        ctx.fireChannelUnregistered();
    }

    /**
     * 读事件完成
     *
     * @param ctx 通道处理器
     * @throws Exception 异常
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("reading event complete connection ID: " + getConnectionID(ctx));
        ctx.flush();
    }

    /**
     * 服务端异常
     *
     * @param ctx   通道处理器
     * @param cause 异常类型
     * @throws Exception 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("socket server error: " + cause.getMessage());
        ctx.close();
    }

    /**
     * 客户端心跳检测
     *
     * @param ctx 通道处理器
     * @param evt 事件
     * @throws Exception 异常
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state().equals(IdleState.READER_IDLE)) {
                // 空闲30s之后触发 (心跳包丢失)
                if (counter >= 2) {
                    // 连续丢失3个心跳包 (超时数据上传)
                    ctx.channel().close().sync();
                    log.error("client connection ID: " + getConnectionID(ctx) + " upload data exception");
                } else {
                    counter++;
                    log.warn("lost package " + getConnectionID(ctx) + " number: " + counter);
                }
            }
        }
    }

    /**
     * 获取客户端连接ID
     *
     * @param ctx 通道处理器
     * @return 信息
     */
    private String getConnectionID(ChannelHandlerContext ctx) {
        return ctx.channel().id().asLongText();
    }
}

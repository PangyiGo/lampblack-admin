package com.osen.cloud.system.data_socket.handler;

import cn.hutool.core.map.MapUtil;
import com.osen.cloud.system.data_socket.utils.DataSegmentParseUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * User: PangYi
 * Date: 2019-09-04
 * Time: 14:54
 * Description: 客户端连接处理
 */
@Slf4j
public class SocketServerHandler extends ChannelInboundHandlerAdapter {

    private DataSegmentParseUtil dataSegmentParseUtil;

    public SocketServerHandler(DataSegmentParseUtil dataSegmentParseUtil) {
        this.dataSegmentParseUtil = dataSegmentParseUtil;
    }

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

        // 心跳监测值
        counter = 0;

        log.info("服务器接收设备上传数据开始：" + getConnectionID(ctx));
        log.info("接收设备数据：" + msg.toString());

        /*
            上传数据处理
         */
        Map<String, Object> parseDataTOMap = dataSegmentParseUtil.parseDataTOMap(msg.toString());

        if (MapUtil.isNotEmpty(parseDataTOMap)) {
            log.info("格式化数据：" + parseDataTOMap);
            dataSegmentParseUtil.chooseHandlerType(parseDataTOMap, getConnectionID(ctx));
        } else {
            log.info("设备上传格式错误，不符合HJ212协议");
        }
    }

    /**
     * 客户端注册连接
     *
     * @param ctx 通道处理器
     * @throws Exception 异常
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("设备成功连接服务器：" + getConnectionID(ctx));
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
        log.info("设备与服务器断开连接：" + getConnectionID(ctx));
        dataSegmentParseUtil.disConnectionDevice(getConnectionID(ctx));
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
        log.info("服务器接收设备上传数据完毕：" + getConnectionID(ctx));
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
        cause.printStackTrace();
        log.info("服务端接收数据异常：" + cause.getMessage());
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
                if (counter > 2) {
                    // 连续丢失3个心跳包 (超时数据上传)
                    ctx.channel().close().sync();
                    log.info("设备数据上传超时：" + getConnectionID(ctx));
                } else {
                    counter++;
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

package com.serotonin.bacnet4j;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.npdu.ip.IpNetworkBuilder;
import com.serotonin.bacnet4j.npdu.ip.IpNetworkUtils;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.service.confirmed.WritePropertyRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.RemoteDeviceFinder;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import java.time.Duration;
import java.util.Optional;

/**
 * @author qxq
 * @date 2024/7/26
 */
public class DirectDeviceTest {

    /**
     * 直连方式读写bacnet
     */
    @Test
    public void connect() throws Exception {

        // 本地设备的网络配置，
        IpNetwork network = new IpNetworkBuilder()
                .withSubnet("255.255.255.0", 24)
                .withPort(47808)
                .withReuseAddress(true)
                .build();

        int localDeviceId = RandomUtils.nextInt() % 10000;

        // 初始化本地设备
        LocalDevice localDevice = new LocalDevice(localDeviceId, new DefaultTransport(network));
        // 初始化
        localDevice.initialize();

        String deviceIp = "192.168.0.220";
        int port = 47808;
        int deviceId = 2605;

        Address address = IpNetworkUtils.toAddress(deviceIp, port);

//        localDevice.send(address, new WhoIsRequest(deviceId, deviceId));
//        RemoteDeviceFinder.RemoteDeviceFuture future = localDevice.getRemoteDevice(deviceId);
//
//        RemoteDevice remoteDevice = future.get(Duration.ofSeconds(10L).toMillis());
//
//        Address reqAddress = remoteDevice.getAddress();

        ServiceFuture send = localDevice.send(address,
                new WritePropertyRequest(new ObjectIdentifier(ObjectType.multiStateOutput, 645), PropertyIdentifier.forName("present-value"),
                        null, new UnsignedInteger(2), null)
        );
        AcknowledgementService acknowledgementService = send.get();


        ReadPropertyAck presentValue = localDevice.send(address,
                new ReadPropertyRequest(new ObjectIdentifier(ObjectType.multiStateOutput, 645),
                        PropertyIdentifier.presentValue)
        ).get();

        Object value = presentValue.getValue();

        System.out.println("value :" + value);
    }

}

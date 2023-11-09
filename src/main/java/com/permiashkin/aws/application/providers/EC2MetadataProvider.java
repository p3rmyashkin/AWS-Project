package com.permiashkin.aws.application.providers;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.util.EC2MetadataUtils;
import com.permiashkin.aws.application.model.dtos.EC2MetadataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class EC2MetadataProvider {

    private final Object LOCK = new Object();

    private final AmazonEC2 amazonEC2;

    private String ipAddress;

    public EC2MetadataDto provideMetadata() {
        return EC2MetadataDto.builder()
                .availabilityZone(wrap(EC2MetadataUtils::getAvailabilityZone))
                .instanceRegion(wrap(EC2MetadataUtils::getEC2InstanceRegion))
                .build();
    }

    public String getPublicIpAddress() {
        try {
            if (this.ipAddress != null) {
                return this.ipAddress;
            }

            synchronized (LOCK) {

                if (this.ipAddress != null) {
                    return this.ipAddress;
                }

                DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest()
                        .withInstanceIds(EC2MetadataUtils.getInstanceId());

                this.ipAddress = amazonEC2
                        .describeInstances(describeInstancesRequest)
                        .getReservations()
                        .stream()
                        .map(Reservation::getInstances)
                        .flatMap(List::stream)
                        .findFirst()
                        .map(Instance::getPublicIpAddress)
                        .orElse(null);

                log.info("Instance's public ip address: {}", this.ipAddress);

                return this.ipAddress;
            }
        } catch (Exception e) {
            log.warn("Exception occurred while trying to resolve public ip address");
            log.debug("Exception occurred while trying to resolve public ip address", e);
            return null;
        }
    }


    private static <T> T wrap(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            log.warn("Exception occurred while executing supplier", e);
            return null;
        }
    }
}

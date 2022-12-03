package com.ergflip.ergflipbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties
public class Transaction {
    private String id;
    private String blockId;
    private Long inclusionHeight;
    private Long timestamp;
    private Long index;
    private Long globalIndex;
    private Long numConfirmations;
    private List<Output> outputs;
    private Long size;

    private String senderAddress;

    public Transaction(String id, String blockId, Long inclusionHeight, Long timestamp, Long index, Long globalIndex, Long numConfirmations, List<Output> outputs, Long size, String senderAddress) {
        this.id = id;
        this.blockId = blockId;
        this.inclusionHeight = inclusionHeight;
        this.timestamp = timestamp;
        this.index = index;
        this.globalIndex = globalIndex;
        this.numConfirmations = numConfirmations;
        this.outputs = outputs;
        this.size = size;
        this.senderAddress = senderAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public Long getInclusionHeight() {
        return inclusionHeight;
    }

    public void setInclusionHeight(Long inclusionHeight) {
        this.inclusionHeight = inclusionHeight;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Long getGlobalIndex() {
        return globalIndex;
    }

    public void setGlobalIndex(Long globalIndex) {
        this.globalIndex = globalIndex;
    }

    public Long getNumConfirmations() {
        return numConfirmations;
    }

    public void setNumConfirmations(Long numConfirmations) {
        this.numConfirmations = numConfirmations;
    }

    public List<Output> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<Output> outputs) {
        this.outputs = outputs;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", blockId='" + blockId + '\'' +
                ", inclusionHeight=" + inclusionHeight +
                ", timestamp=" + timestamp +
                ", index=" + index +
                ", globalIndex=" + globalIndex +
                ", numConfirmations=" + numConfirmations +
                ", outputs=" + outputs +
                ", size=" + size +
                '}';
    }
}

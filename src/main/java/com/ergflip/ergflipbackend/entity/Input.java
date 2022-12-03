package com.ergflip.ergflipbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

@JsonIgnoreProperties
public class Input {
    private String boxId;
    private Long value;
    private Long index;
    private String spendingProof;
    private String outputBlockId;
    private String outputTransactionId;
    private Long outputIndex;
    private Long outputGlobalIndex;
    private Long outputCreatedAt;
    private Long outputSettledAt;
    private String ergoTree;
    private String address;
    private Object[] assets;
    private Register[] additionalRegisters;

    public Input(String boxId, Long value, Long index, String spendingProof, String outputBlockId, String outputTransactionId, Long outputIndex, Long outputGlobalIndex, Long outputCreatedAt, Long outputSettledAt, String ergoTree, String address, Object[] assets, Register[] additionalRegisters) {
        this.boxId = boxId;
        this.value = value;
        this.index = index;
        this.spendingProof = spendingProof;
        this.outputBlockId = outputBlockId;
        this.outputTransactionId = outputTransactionId;
        this.outputIndex = outputIndex;
        this.outputGlobalIndex = outputGlobalIndex;
        this.outputCreatedAt = outputCreatedAt;
        this.outputSettledAt = outputSettledAt;
        this.ergoTree = ergoTree;
        this.address = address;
        this.assets = assets;
        this.additionalRegisters = additionalRegisters;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getSpendingProof() {
        return spendingProof;
    }

    public void setSpendingProof(String spendingProof) {
        this.spendingProof = spendingProof;
    }

    public String getOutputBlockId() {
        return outputBlockId;
    }

    public void setOutputBlockId(String outputBlockId) {
        this.outputBlockId = outputBlockId;
    }

    public String getOutputTransactionId() {
        return outputTransactionId;
    }

    public void setOutputTransactionId(String outputTransactionId) {
        this.outputTransactionId = outputTransactionId;
    }

    public Long getOutputIndex() {
        return outputIndex;
    }

    public void setOutputIndex(Long outputIndex) {
        this.outputIndex = outputIndex;
    }

    public Long getOutputGlobalIndex() {
        return outputGlobalIndex;
    }

    public void setOutputGlobalIndex(Long outputGlobalIndex) {
        this.outputGlobalIndex = outputGlobalIndex;
    }

    public Long getOutputCreatedAt() {
        return outputCreatedAt;
    }

    public void setOutputCreatedAt(Long outputCreatedAt) {
        this.outputCreatedAt = outputCreatedAt;
    }

    public Long getOutputSettledAt() {
        return outputSettledAt;
    }

    public void setOutputSettledAt(Long outputSettledAt) {
        this.outputSettledAt = outputSettledAt;
    }

    public String getErgoTree() {
        return ergoTree;
    }

    public void setErgoTree(String ergoTree) {
        this.ergoTree = ergoTree;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object[] getAssets() {
        return assets;
    }

    public void setAssets(Object[] assets) {
        this.assets = assets;
    }

    public Register[] getAdditionalRegisters() {
        return additionalRegisters;
    }

    public void setAdditionalRegisters(Register[] additionalRegisters) {
        this.additionalRegisters = additionalRegisters;
    }

    @Override
    public String toString() {
        return "Input{" +
                "boxId='" + boxId + '\'' +
                ", value=" + value +
                ", index=" + index +
                ", spendingProof='" + spendingProof + '\'' +
                ", outputBlockId='" + outputBlockId + '\'' +
                ", outputTransactionId='" + outputTransactionId + '\'' +
                ", outputIndex=" + outputIndex +
                ", outputGlobalIndex=" + outputGlobalIndex +
                ", outputCreatedAt=" + outputCreatedAt +
                ", outputSettledAt=" + outputSettledAt +
                ", ergoTree='" + ergoTree + '\'' +
                ", address='" + address + '\'' +
                ", assets=" + Arrays.toString(assets) +
                ", additionalRegisters=" + Arrays.toString(additionalRegisters) +
                '}';
    }
}

package com.ergflip.ergflipbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class Output {
    private String boxId;
    private String transactionId;
    private String blockId;
    private Long value;
    private Long index;
    private Long globalIndex;
    private Long creationHeight;
    private Long settlementHeight;
    private String ergoTree;
    private String address;
/*    private Register[] additionalRegisters;*/
    private String spentTransactionId;
    private boolean mainChain;

    public Output(String boxId, String transactionId, String blockId, Long value, Long index, Long globalIndex, Long creationHeight, Long settlementHeight, String ergoTree, String address, /*Register[] additionalRegisters,*/ String spentTransactionId, boolean mainChain) {
        this.boxId = boxId;
        this.transactionId = transactionId;
        this.blockId = blockId;
        this.value = value;
        this.index = index;
        this.globalIndex = globalIndex;
        this.creationHeight = creationHeight;
        this.settlementHeight = settlementHeight;
        this.ergoTree = ergoTree;
        this.address = address;
/*        this.additionalRegisters = additionalRegisters;*/
        this.spentTransactionId = spentTransactionId;
        this.mainChain = mainChain;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
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

    public Long getGlobalIndex() {
        return globalIndex;
    }

    public void setGlobalIndex(Long globalIndex) {
        this.globalIndex = globalIndex;
    }

    public Long getCreationHeight() {
        return creationHeight;
    }

    public void setCreationHeight(Long creationHeight) {
        this.creationHeight = creationHeight;
    }

    public Long getSettlementHeight() {
        return settlementHeight;
    }

    public void setSettlementHeight(Long settlementHeight) {
        this.settlementHeight = settlementHeight;
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

/*    public Register[] getAdditionalRegisters() {
        return additionalRegisters;
    }

    public void setAdditionalRegisters(Register[] additionalRegisters) {
        this.additionalRegisters = additionalRegisters;
    }*/

    public String getSpentTransactionId() {
        return spentTransactionId;
    }

    public void setSpentTransactionId(String spentTransactionId) {
        this.spentTransactionId = spentTransactionId;
    }

    public boolean isMainChain() {
        return mainChain;
    }

    public void setMainChain(boolean mainChain) {
        this.mainChain = mainChain;
    }

    @Override
    public String toString() {
        return "Output{" +
                "boxId='" + boxId + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", blockId='" + blockId + '\'' +
                ", value=" + value +
                ", index=" + index +
                ", globalIndex=" + globalIndex +
                ", creationHeight=" + creationHeight +
                ", settlementHeight=" + settlementHeight +
                ", ergoTree='" + ergoTree + '\'' +
                ", address='" + address + '\'' +
/*                ", additionalRegisters=" + Arrays.toString(additionalRegisters) +*/
                ", spentTransactionId='" + spentTransactionId + '\'' +
                ", mainChain=" + mainChain +
                '}';
    }
}

package pojo;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table card_trade
 *
 * @mbggenerated do_not_delete_during_merge
 */
public class CardTrade {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column card_trade.card_id
     *
     * @mbggenerated
     */
    private Long cardId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column card_trade.trade_id
     *
     * @mbggenerated
     */
    private Long tradeId;

    /**
     * Database Column Remarks:
     *   0:pay/1:receive
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column card_trade.trade_type
     *
     * @mbggenerated
     */
    private Integer tradeType;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column card_trade.card_id
     *
     * @return the value of card_trade.card_id
     *
     * @mbggenerated
     */
    public Long getCardId() {
        return cardId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column card_trade.card_id
     *
     * @param cardId the value for card_trade.card_id
     *
     * @mbggenerated
     */
    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column card_trade.trade_id
     *
     * @return the value of card_trade.trade_id
     *
     * @mbggenerated
     */
    public Long getTradeId() {
        return tradeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column card_trade.trade_id
     *
     * @param tradeId the value for card_trade.trade_id
     *
     * @mbggenerated
     */
    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column card_trade.trade_type
     *
     * @return the value of card_trade.trade_type
     *
     * @mbggenerated
     */
    public Integer getTradeType() {
        return tradeType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column card_trade.trade_type
     *
     * @param tradeType the value for card_trade.trade_type
     *
     * @mbggenerated
     */
    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }
}
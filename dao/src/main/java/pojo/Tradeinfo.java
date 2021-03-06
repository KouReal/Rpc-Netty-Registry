package pojo;

import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table tradeinfo
 *
 * @mbggenerated do_not_delete_during_merge
 */
public class Tradeinfo {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tradeinfo.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tradeinfo.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tradeinfo.amount
     *
     * @mbggenerated
     */
    private Long amount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tradeinfo.payer
     *
     * @mbggenerated
     */
    private Long payer;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tradeinfo.receiver
     *
     * @mbggenerated
     */
    private Long receiver;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tradeinfo.id
     *
     * @return the value of tradeinfo.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tradeinfo.id
     *
     * @param id the value for tradeinfo.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tradeinfo.create_time
     *
     * @return the value of tradeinfo.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tradeinfo.create_time
     *
     * @param createTime the value for tradeinfo.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tradeinfo.amount
     *
     * @return the value of tradeinfo.amount
     *
     * @mbggenerated
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tradeinfo.amount
     *
     * @param amount the value for tradeinfo.amount
     *
     * @mbggenerated
     */
    public void setAmount(Long amount) {
        this.amount = amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tradeinfo.payer
     *
     * @return the value of tradeinfo.payer
     *
     * @mbggenerated
     */
    public Long getPayer() {
        return payer;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tradeinfo.payer
     *
     * @param payer the value for tradeinfo.payer
     *
     * @mbggenerated
     */
    public void setPayer(Long payer) {
        this.payer = payer;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tradeinfo.receiver
     *
     * @return the value of tradeinfo.receiver
     *
     * @mbggenerated
     */
    public Long getReceiver() {
        return receiver;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tradeinfo.receiver
     *
     * @param receiver the value for tradeinfo.receiver
     *
     * @mbggenerated
     */
    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }
}
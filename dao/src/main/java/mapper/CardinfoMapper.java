package mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import pojo.Cardinfo;
import pojo.CardinfoExample;

public interface CardinfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cardinfo
     *
     * @mbggenerated
     */
    int countByExample(CardinfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cardinfo
     *
     * @mbggenerated
     */
    int deleteByExample(CardinfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cardinfo
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cardinfo
     *return cardid
     * @mbggenerated
     */
    Long insert(Cardinfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cardinfo
     *
     * @mbggenerated
     */
    int insertSelective(Cardinfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cardinfo
     *
     * @mbggenerated
     */
    List<Cardinfo> selectByExample(CardinfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cardinfo
     *
     * @mbggenerated
     */
    Cardinfo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cardinfo
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") Cardinfo record, @Param("example") CardinfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cardinfo
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") Cardinfo record, @Param("example") CardinfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cardinfo
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Cardinfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cardinfo
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Cardinfo record);
}
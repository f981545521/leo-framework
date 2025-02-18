package cn.acyou.leo.product.es.eo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * @author youfang
 * @version [1.0.0, 2021-8-7]
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "product")
public class ProductEo implements Serializable {

    @Id
    //注意 ES中的_id 必须要是id字段
    private Long id;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String productName;

    @Field(type = FieldType.Integer)
    private Integer stockNumber;

    @Field(type = FieldType.Integer)
    private Integer status;
}

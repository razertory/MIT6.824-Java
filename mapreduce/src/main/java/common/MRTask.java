package common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MRTask {

    private Integer id;
    private Integer taskType;
    private Integer status;
    private String file;
    private Integer reduceNum;
    private Integer mapNum;
    private String reduceOutFile;
}

package com.sxnsh.exam.service;

import com.sxnsh.exam.constant.QuesContent;
import com.sxnsh.exam.entity.ExamOptions;
import com.sxnsh.exam.entity.ExamQuestion;
import com.sxnsh.exam.mapper.ExamMapper;
import com.sxnsh.exam.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yexz
 * @date 2020/3/17
 */
@Slf4j
@Service
public class FileService {

    @Resource
    ExamMapper examMapper;
    /**
     *  * fetch data
     * @desc  将excel文件转化为题库数据
     * @param  filePath
     * @return int 0-失败  1-成功
     */
    public int transFileToData(String filePath){
        int result = 0;
        if (filePath != null && !"".equals(filePath)) {
            try{
                File questionFile = new File(filePath);
                InputStream is = FileMagic.prepareToCheckMagic(new FileInputStream(questionFile));
                /**判断题库文件是xls格式的后进行文件的解析，将题库写入数据库**/
                if(filePath.endsWith(".xls") && FileMagic.valueOf(is) == FileMagic.OLE2){
                    FileInputStream fileInputStream = new FileInputStream(filePath);
                    HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);

                    int sheetNum = workbook.getNumberOfSheets();
                    HSSFSheet quesheet = null;
                    for(int i = 0; i < sheetNum; i ++){
                        if(workbook.getSheetName(i).trim() != null && "试题库".equals(workbook.getSheetName(i).trim())){
                            quesheet = workbook.getSheetAt(i);
                            break;
                        }
                    }

                    if(quesheet != null){
                        HSSFRow headRow = quesheet.getRow(1);
                        if(ExcelUtil.getCellValue(headRow.getCell(0)) != null && "序号".equals(ExcelUtil.getCellValue(headRow.getCell(0)))){
                            for(int k = 2; k < quesheet.getLastRowNum() + 1; k++ ){
                                HSSFRow tempRow = quesheet.getRow(k);
                                ExamQuestion examQuestion = new ExamQuestion();

                                /**处理题目信息-----------start**/
                                /**获取当前插入的最新的序号 --取得是当前sqlsession中最新插入的id数据，当插入多个表时会不正确或者查出来多个值**/
//                                int lastQuesId = examMapper.findLastInsertId("exam_question");

                                /**获取题目的信息**/
                                examQuestion.setContent(ExcelUtil.getCellValue(tempRow.getCell(1)));
                                examQuestion.setAnswer(ExcelUtil.getCellValue(tempRow.getCell(3)));

                                /**处理题目类型**/
                                String tempType= ExcelUtil.getCellValue(tempRow.getCell(4));
                                if(tempType != null && !"".equals(tempType)){
                                    examQuestion.setType(QuesContent.questionType.get(tempType));
                                    /**判断题需要将正确和错误转换为1或者0**/
                                    if("J".equals(examQuestion.getType())){
                                        examQuestion.setAnswer(QuesContent.optionRightOrWrong.get(ExcelUtil.getCellValue(tempRow.getCell(3))));
                                    }
                                }else{
                                    /**否则设置为未知**/
                                    examQuestion.setType("O");
                                }

                                /**处理题目范围**/
                                String tempScope = ExcelUtil.getCellValue(tempRow.getCell(5));
                                if(tempScope != null && !"".equals(tempScope)){
                                    examQuestion.setScope(QuesContent.questionScope.get(tempScope));
                                }else{
                                    /**否则设置为未知**/
                                    examQuestion.setScope("O");
                                }

                                /**问题写入数据库**/
                                examMapper.insertQuestion(examQuestion);
                                int lastestQuesId = examQuestion.getId();
                                /**处理题目信息-----------end**/

                                /**处理选项-----------start**/
                                /**选择题好处理一些，直接放正确和错误的两个选项就行**/
                                if("J".equals(examQuestion.getType())){
                                    List<ExamOptions> options = new ArrayList<ExamOptions>();
                                    ExamOptions rightOption = new ExamOptions();
                                    rightOption.setContent("正确");
                                    rightOption.setOptionNum("1");
                                    rightOption.setQuestionId(lastestQuesId + "");

                                    ExamOptions wrongOption = new ExamOptions();
                                    wrongOption.setContent("错误");
                                    wrongOption.setOptionNum("0");
                                    wrongOption.setQuestionId(lastestQuesId + "");

                                    /**将选项插入数据库**/
                                    examMapper.inserOption(rightOption);
                                    examMapper.inserOption(wrongOption);
                                 /**处理单选题和多选题的选项**/
                                }else if("S".equals(examQuestion.getType()) || "M".equals(examQuestion.getType())){
                                    List<ExamOptions> optionList = new ArrayList<ExamOptions>();
                                    String optionContent = ExcelUtil.getCellValue(tempRow.getCell(2));
                                    String[] options = optionContent.split("\\n");
                                    for(int j = 0 ; j < options.length; j++){
                                        ExamOptions tempOption = new ExamOptions();
                                        tempOption.setQuestionId(lastestQuesId + "");
                                        tempOption.setOptionNum(options[j].substring(0,options[j].indexOf("、")));
                                        tempOption.setContent(options[j].substring(options[j].indexOf("、") + 1));

                                        optionList.add(tempOption);
                                    }

                                    /**将选项插入数据库**/
                                    examMapper.insertOptions(optionList);
                                }
                                result = 1;

                                /**处理选项-----------end**/

                            }
                        }
                    }


                }


            }catch (Exception e) {
                e.printStackTrace();
                result = 0;
            }
        }

        return result;
    }

}

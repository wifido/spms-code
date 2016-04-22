package com.sf.module.esbinterface.util;

import com.sf.module.esbinterface.domain.ScheduleWithErrorInfo;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class XmlParserTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private File xmlFile;

    @Before
    public void setup() {
        try {
            xmlFile = temporaryFolder.newFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @After
    public void tearDown() {
        if (xmlFile != null)
            xmlFile.deleteOnExit();
    }

    private String preparedScheduleAsXml =
            "<?xml version='1.0' encoding='UTF-8'?>\n" +
                    "<ZSHR_WORK_TIME_FILE>\n" +
                    "    <COMMENT/>\n" +
                    "    <DOCUMENTS>\n" +
                    "        <item>\n" +
                    "            <ZHRXH>1</ZHRXH>\n" +
                    "            <PERNR>460873</PERNR>\n" +
                    "            <BEGDA>20141002</BEGDA>\n" +
                    "            <ENDDA>20141002</ENDDA>\n" +
                    "            <BEGUZ>060000</BEGUZ>\n" +
                    "            <ENDUZ>080000</ENDUZ>\n" +
                    "            <VTKEN/>\n" +
                    "            <TPROG/>\n" +
                    "            <ZHRPBXT>2</ZHRPBXT>\n" +
                    "            <ZHRCLBZ/>\n" +
                    "            <MESSAGE>ERROR</MESSAGE>" +
                    "        </item>\n" +
                    "        <item\n" +
                    "            <ZHRXH>2</ZHRXH>\n" +
                    "            <PERNR>460872</PERNR>\n" +
                    "            <BEGDA>20141003</BEGDA>\n" +
                    "            <ENDDA>20141003</ENDDA>\n" +
                    "            <BEGUZ>234000</BEGUZ>\n" +
                    "            <ENDUZ>043000</ENDUZ>\n" +
                    "            <VTKEN/>\n" +
                    "            <TPROG/>\n" +
                    "            <ZHRPBXT>2</ZHRPBXT>\n" +
                    "            <ZHRCLBZ/>\n" +
                    "            <MESSAGE>ERROR</MESSAGE>" +
                    "        </item>\n" +
                    "    </DOCUMENTS>\n" +
                    "</ZSHR_WORK_TIME_FILE>\n";

    @Test
    public void should_specify_enum_order() {
        assertThat(XmlParser.XmlNodeType.START_ELEMENT.value).isEqualTo(1);
        assertThat(XmlParser.XmlNodeType.CHARACTERS.value).isEqualTo(4);
        assertThat(XmlParser.XmlNodeType.END_ELEMENT.value).isEqualTo(2);
    }

    @Test
    @Ignore
    /**
     * Todo
     */
    public void should_parse_schedule_error_information() {
        //given
        pretendFileWithSpecifyContent(preparedScheduleAsXml);
        XmlParser xmlParser = new XmlParser(new XmlParser.OnSaveHandler() {
            @Override
            public void save(List beans) {

            }
        });

        //when
        List<ScheduleWithErrorInfo> scheduleWithErrorInfoList = xmlParser.parserScheduleError(xmlFile);

        //then
        assertThat(scheduleWithErrorInfoList).hasSize(2);
        ScheduleWithErrorInfo actualFirst = scheduleWithErrorInfoList.get(0);
        assertThat(actualFirst.getEmpCode()).isEqualTo("460873");
        assertThat(actualFirst.getBeginDate()).isEqualTo("20141002");
        assertThat(actualFirst.getEndDate()).isEqualTo("20141002");
        assertThat(actualFirst.getBeginTime()).isEqualTo("060000");
        assertThat(actualFirst.getEndTime()).isEqualTo("080000");
        assertThat(actualFirst.getClassSystem()).isEqualTo("2");
        assertThat(actualFirst.getError()).isEqualTo("ERROR");
    }

    private void pretendFileWithSpecifyContent(String content) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(xmlFile);
            fileWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
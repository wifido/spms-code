package com.sf.module.esbinterface.util;

import com.sf.module.esbinterface.domain.HrEmpInfo;
import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class HREmpXMLReaderTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void testReader() throws Exception {
        File file = temporaryFolder.newFile();

        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<emps size=\"5\" batch_number=\"20140904091433\" errmsg=\"\">\n" +
                    "\t<emp>\n" +
                    "\t\t<emp_id>33057942</emp_id>\n" +
                    "\t\t<person_id>355687</person_id>\n" +
                    "\t\t<emp_num>G02YYqogCJ7Ujy7TKznTNw==</emp_num>\n" +
                    "\t\t<last_name>G02YYqogCJ7Ujy7TKznTNw==</last_name>\n" +
                    "\t\t<first_name>0Uc90ObGmiimSQSYki6Vsg==</first_name>\n" +
                    "\t\t<sex>M</sex>\n" +
                    "\t\t<curr_org_id>2638</curr_org_id>\n" +
                    "\t\t<net_code>020MB</net_code>\n" +
                    "\t\t<curr_org_name>020MB.会展分部琶洲点部</curr_org_name>\n" +
                    "\t\t<org_code>020MB</org_code>\n" +
                    "\t\t<curr_area>广州区</curr_area>\n" +
                    "\t\t<position_id>11329</position_id>\n" +
                    "\t\t<position_name>收派员</position_name>\n" +
                    "\t\t<position_code>DTOPJC078</position_code>\n" +
                    "\t\t<job_id>68</job_id>\n" +
                    "\t\t<job_name>基层员工</job_name>\n" +
                    "\t\t<postal_code></postal_code>\n" +
                    "\t\t<address></address>\n" +
                    "\t\t<mail_address></mail_address>\n" +
                    "\t\t<mobile_phone>13570117376</mobile_phone>\n" +
                    "\t\t<office_phone></office_phone>\n" +
                    "\t\t<job_date_from>2006-9-1</job_date_from>\n" +
                    "\t\t<sf_date>2010-3-12</sf_date>\n" +
                    "\t\t<cancel_date></cancel_date>\n" +
                    "\t\t<cancel_flag>N</cancel_flag>\n" +
                    "\t\t<date_from>2012-12-1</date_from>\n" +
                    "\t\t<last_zno></last_zno>\n" +
                    "\t\t<last_org_name></last_org_name>\n" +
                    "\t\t<person_type>个人承包经营者</person_type>\n" +
                    "\t\t<emp_category>正式</emp_category>\n" +
                    "\t\t<office_addr>广东广州</office_addr>\n" +
                    "\t\t<edu_level>大专</edu_level>\n" +
                    "\t\t<effective_date>2012-12-1</effective_date>\n" +
                    "\t\t<position_type>操作类</position_type>\n" +
                    "\t\t<position_group>营运</position_group>\n" +
                    "\t\t<position_attr>一线</position_attr>\n" +
                    "\t\t<speciality></speciality>\n" +
                    "\t\t<supervisor_number>004583</supervisor_number>\n" +
                    "\t\t<supervisor_name>004583</supervisor_name>\n" +
                    "\t\t<emp_cate_code>CN_ZS</emp_cate_code>\n" +
                    "\t\t<ass_category></ass_category>\n" +
                    "\t\t<ass_reason></ass_reason>\n" +
                    "\t\t<leaving_reason></leaving_reason>\n" +
                    "\t\t<vacation_start_date></vacation_start_date>\n" +
                    "\t\t<date_of_birth>1986-1-25</date_of_birth>\n" +
                    "\t\t<cn_race>汉族</cn_race>\n" +
                    "\t\t<hukou_local>其它</hukou_local>\n" +
                    "\t\t<blood_type>A</blood_type>\n" +
                    "\t\t<marital_status>单身</marital_status>\n" +
                    "\t\t<probation_end_date></probation_end_date>\n" +
                    "\t\t<attribute1>中国</attribute1>\n" +
                    "\t\t<attribute5></attribute5>\n" +
                    "\t\t<attribute6></attribute6>\n" +
                    "\t\t<attribute7></attribute7>\n" +
                    "\t\t<attribute8></attribute8>\n" +
                    "\t\t<attribute9></attribute9>\n" +
                    "\t\t<attribute10></attribute10>\n" +
                    "\t\t<insert_time>2014-8-29 8:48:52</insert_time>\n" +
                    "\t\t<receive_system></receive_system>\n" +
                    "\t\t<stature>173</stature>\n" +
                    "\t\t<weight>62</weight>\n" +
                    "\t\t<recruit_channel>报纸广告</recruit_channel>\n" +
                    "\t\t<channel_name>广州日报</channel_name>\n" +
                    "\t\t<ancestral_home>广东省</ancestral_home>\n" +
                    "\t\t<family_phone></family_phone>\n" +
                    "\t\t<old_last_name></old_last_name>\n" +
                    "\t\t<registered_disabled_flag>否</registered_disabled_flag>\n" +
                    "\t</emp>\n" +
                    "\t<emp>\n" +
                    "\t\t<emp_id>33057943</emp_id>\n" +
                    "\t\t<person_id>748002</person_id>\n" +
                    "\t\t<emp_num>rWeSndCDZeL4i/VU4VhhIg==</emp_num>\n" +
                    "\t\t<last_name>rWeSndCDZeL4i/VU4VhhIg==</last_name>\n" +
                    "\t\t<first_name>0Uc90ObGmiimSQSYki6Vsg==</first_name>\n" +
                    "\t\t<sex>M</sex>\n" +
                    "\t\t<curr_org_id>2638</curr_org_id>\n" +
                    "\t\t<net_code>020MB</net_code>\n" +
                    "\t\t<curr_org_name>020MB.会展分部琶洲点部</curr_org_name>\n" +
                    "\t\t<org_code>020MB</org_code>\n" +
                    "\t\t<curr_area>广州区</curr_area>\n" +
                    "\t\t<position_id>11329</position_id>\n" +
                    "\t\t<position_name>收派员</position_name>\n" +
                    "\t\t<position_code>DTOPJC078</position_code>\n" +
                    "\t\t<job_id>68</job_id>\n" +
                    "\t\t<job_name>基层员工</job_name>\n" +
                    "\t\t<postal_code></postal_code>\n" +
                    "\t\t<address></address>\n" +
                    "\t\t<mail_address></mail_address>\n" +
                    "\t\t<mobile_phone>13570062648</mobile_phone>\n" +
                    "\t\t<office_phone></office_phone>\n" +
                    "\t\t<job_date_from>2005-10-1</job_date_from>\n" +
                    "\t\t<sf_date>2011-6-1</sf_date>\n" +
                    "\t\t<cancel_date></cancel_date>\n" +
                    "\t\t<cancel_flag>N</cancel_flag>\n" +
                    "\t\t<date_from>2012-12-1</date_from>\n" +
                    "\t\t<last_zno></last_zno>\n" +
                    "\t\t<last_org_name></last_org_name>\n" +
                    "\t\t<person_type>个人承包经营者</person_type>\n" +
                    "\t\t<emp_category>正式</emp_category>\n" +
                    "\t\t<office_addr>广东广州</office_addr>\n" +
                    "\t\t<edu_level>高中/中专</edu_level>\n" +
                    "\t\t<effective_date>2012-12-1</effective_date>\n" +
                    "\t\t<position_type>操作类</position_type>\n" +
                    "\t\t<position_group>营运</position_group>\n" +
                    "\t\t<position_attr>一线</position_attr>\n" +
                    "\t\t<speciality></speciality>\n" +
                    "\t\t<supervisor_number>004583</supervisor_number>\n" +
                    "\t\t<supervisor_name>004583</supervisor_name>\n" +
                    "\t\t<emp_cate_code>CN_ZS</emp_cate_code>\n" +
                    "\t\t<ass_category></ass_category>\n" +
                    "\t\t<ass_reason></ass_reason>\n" +
                    "\t\t<leaving_reason></leaving_reason>\n" +
                    "\t\t<vacation_start_date></vacation_start_date>\n" +
                    "\t\t<date_of_birth>1969-9-21</date_of_birth>\n" +
                    "\t\t<cn_race>汉族</cn_race>\n" +
                    "\t\t<hukou_local>其它</hukou_local>\n" +
                    "\t\t<blood_type></blood_type>\n" +
                    "\t\t<marital_status>已婚</marital_status>\n" +
                    "\t\t<probation_end_date></probation_end_date>\n" +
                    "\t\t<attribute1>中国</attribute1>\n" +
                    "\t\t<attribute5></attribute5>\n" +
                    "\t\t<attribute6></attribute6>\n" +
                    "\t\t<attribute7></attribute7>\n" +
                    "\t\t<attribute8></attribute8>\n" +
                    "\t\t<attribute9></attribute9>\n" +
                    "\t\t<attribute10></attribute10>\n" +
                    "\t\t<insert_time>2014-8-29 8:48:52</insert_time>\n" +
                    "\t\t<receive_system></receive_system>\n" +
                    "\t\t<stature>166</stature>\n" +
                    "\t\t<weight>75</weight>\n" +
                    "\t\t<recruit_channel>报纸广告</recruit_channel>\n" +
                    "\t\t<channel_name>广州日报</channel_name>\n" +
                    "\t\t<ancestral_home>湖南省</ancestral_home>\n" +
                    "\t\t<family_phone>4008111111</family_phone>\n" +
                    "\t\t<old_last_name></old_last_name>\n" +
                    "\t\t<registered_disabled_flag>否</registered_disabled_flag>\n" +
                    "\t</emp>\n" +
                    "\t<emp>\n" +
                    "\t\t<emp_id>33057944</emp_id>\n" +
                    "\t\t<person_id>693341</person_id>\n" +
                    "\t\t<emp_num>e15b3lE+I+4gWMHfejzD1w==</emp_num>\n" +
                    "\t\t<last_name>e15b3lE+I+4gWMHfejzD1w==</last_name>\n" +
                    "\t\t<first_name>0Uc90ObGmiimSQSYki6Vsg==</first_name>\n" +
                    "\t\t<sex>M</sex>\n" +
                    "\t\t<curr_org_id>2638</curr_org_id>\n" +
                    "\t\t<net_code>020MB</net_code>\n" +
                    "\t\t<curr_org_name>020MB.会展分部琶洲点部</curr_org_name>\n" +
                    "\t\t<org_code>020MB</org_code>\n" +
                    "\t\t<curr_area>广州区</curr_area>\n" +
                    "\t\t<position_id>11329</position_id>\n" +
                    "\t\t<position_name>收派员</position_name>\n" +
                    "\t\t<position_code>DTOPJC078</position_code>\n" +
                    "\t\t<job_id>68</job_id>\n" +
                    "\t\t<job_name>基层员工</job_name>\n" +
                    "\t\t<postal_code></postal_code>\n" +
                    "\t\t<address></address>\n" +
                    "\t\t<mail_address></mail_address>\n" +
                    "\t\t<mobile_phone>13924288748</mobile_phone>\n" +
                    "\t\t<office_phone></office_phone>\n" +
                    "\t\t<job_date_from>2009-3-5</job_date_from>\n" +
                    "\t\t<sf_date>2011-5-5</sf_date>\n" +
                    "\t\t<cancel_date></cancel_date>\n" +
                    "\t\t<cancel_flag>N</cancel_flag>\n" +
                    "\t\t<date_from>2012-12-1</date_from>\n" +
                    "\t\t<last_zno></last_zno>\n" +
                    "\t\t<last_org_name></last_org_name>\n" +
                    "\t\t<person_type>个人承包经营者</person_type>\n" +
                    "\t\t<emp_category>正式</emp_category>\n" +
                    "\t\t<office_addr>广东广州</office_addr>\n" +
                    "\t\t<edu_level>高中/中专</edu_level>\n" +
                    "\t\t<effective_date>2012-12-1</effective_date>\n" +
                    "\t\t<position_type>操作类</position_type>\n" +
                    "\t\t<position_group>营运</position_group>\n" +
                    "\t\t<position_attr>一线</position_attr>\n" +
                    "\t\t<speciality></speciality>\n" +
                    "\t\t<supervisor_number>004583</supervisor_number>\n" +
                    "\t\t<supervisor_name>004583</supervisor_name>\n" +
                    "\t\t<emp_cate_code>CN_ZS</emp_cate_code>\n" +
                    "\t\t<ass_category></ass_category>\n" +
                    "\t\t<ass_reason></ass_reason>\n" +
                    "\t\t<leaving_reason></leaving_reason>\n" +
                    "\t\t<vacation_start_date></vacation_start_date>\n" +
                    "\t\t<date_of_birth>1982-8-30</date_of_birth>\n" +
                    "\t\t<cn_race>汉族</cn_race>\n" +
                    "\t\t<hukou_local>其它</hukou_local>\n" +
                    "\t\t<blood_type>B</blood_type>\n" +
                    "\t\t<marital_status>单身</marital_status>\n" +
                    "\t\t<probation_end_date></probation_end_date>\n" +
                    "\t\t<attribute1>中国</attribute1>\n" +
                    "\t\t<attribute5></attribute5>\n" +
                    "\t\t<attribute6></attribute6>\n" +
                    "\t\t<attribute7></attribute7>\n" +
                    "\t\t<attribute8></attribute8>\n" +
                    "\t\t<attribute9></attribute9>\n" +
                    "\t\t<attribute10></attribute10>\n" +
                    "\t\t<insert_time>2014-8-29 8:48:52</insert_time>\n" +
                    "\t\t<receive_system></receive_system>\n" +
                    "\t\t<stature>165</stature>\n" +
                    "\t\t<weight>57</weight>\n" +
                    "\t\t<recruit_channel>报纸广告</recruit_channel>\n" +
                    "\t\t<channel_name>广州日报</channel_name>\n" +
                    "\t\t<ancestral_home>四川省</ancestral_home>\n" +
                    "\t\t<family_phone>4008111111</family_phone>\n" +
                    "\t\t<old_last_name></old_last_name>\n" +
                    "\t\t<registered_disabled_flag>否</registered_disabled_flag>\n" +
                    "\t</emp>\n" +
                    "\t<emp>\n" +
                    "\t\t<emp_id>33057945</emp_id>\n" +
                    "\t\t<person_id>678303</person_id>\n" +
                    "\t\t<emp_num>iqyyOevxa4e+kMrxBNDavw==</emp_num>\n" +
                    "\t\t<last_name>iqyyOevxa4e+kMrxBNDavw==</last_name>\n" +
                    "\t\t<first_name>0Uc90ObGmiimSQSYki6Vsg==</first_name>\n" +
                    "\t\t<sex>M</sex>\n" +
                    "\t\t<curr_org_id>2638</curr_org_id>\n" +
                    "\t\t<net_code>020MB</net_code>\n" +
                    "\t\t<curr_org_name>020MB.会展分部琶洲点部</curr_org_name>\n" +
                    "\t\t<org_code>020MB</org_code>\n" +
                    "\t\t<curr_area>广州区</curr_area>\n" +
                    "\t\t<position_id>11329</position_id>\n" +
                    "\t\t<position_name>收派员</position_name>\n" +
                    "\t\t<position_code>DTOPJC078</position_code>\n" +
                    "\t\t<job_id>68</job_id>\n" +
                    "\t\t<job_name>基层员工</job_name>\n" +
                    "\t\t<postal_code></postal_code>\n" +
                    "\t\t<address></address>\n" +
                    "\t\t<mail_address></mail_address>\n" +
                    "\t\t<mobile_phone>13924288604</mobile_phone>\n" +
                    "\t\t<office_phone></office_phone>\n" +
                    "\t\t<job_date_from>2003-2-9</job_date_from>\n" +
                    "\t\t<sf_date>2011-4-21</sf_date>\n" +
                    "\t\t<cancel_date></cancel_date>\n" +
                    "\t\t<cancel_flag>N</cancel_flag>\n" +
                    "\t\t<date_from>2012-12-1</date_from>\n" +
                    "\t\t<last_zno></last_zno>\n" +
                    "\t\t<last_org_name></last_org_name>\n" +
                    "\t\t<person_type>个人承包经营者</person_type>\n" +
                    "\t\t<emp_category>正式</emp_category>\n" +
                    "\t\t<office_addr>广东广州</office_addr>\n" +
                    "\t\t<edu_level>高中/中专</edu_level>\n" +
                    "\t\t<effective_date>2012-12-1</effective_date>\n" +
                    "\t\t<position_type>操作类</position_type>\n" +
                    "\t\t<position_group>营运</position_group>\n" +
                    "\t\t<position_attr>一线</position_attr>\n" +
                    "\t\t<speciality></speciality>\n" +
                    "\t\t<supervisor_number>004583</supervisor_number>\n" +
                    "\t\t<supervisor_name>004583</supervisor_name>\n" +
                    "\t\t<emp_cate_code>CN_ZS</emp_cate_code>\n" +
                    "\t\t<ass_category></ass_category>\n" +
                    "\t\t<ass_reason></ass_reason>\n" +
                    "\t\t<leaving_reason></leaving_reason>\n" +
                    "\t\t<vacation_start_date></vacation_start_date>\n" +
                    "\t\t<date_of_birth>1981-1-28</date_of_birth>\n" +
                    "\t\t<cn_race>汉族</cn_race>\n" +
                    "\t\t<hukou_local>其它</hukou_local>\n" +
                    "\t\t<blood_type></blood_type>\n" +
                    "\t\t<marital_status>已婚</marital_status>\n" +
                    "\t\t<probation_end_date></probation_end_date>\n" +
                    "\t\t<attribute1>中国</attribute1>\n" +
                    "\t\t<attribute5></attribute5>\n" +
                    "\t\t<attribute6></attribute6>\n" +
                    "\t\t<attribute7></attribute7>\n" +
                    "\t\t<attribute8></attribute8>\n" +
                    "\t\t<attribute9></attribute9>\n" +
                    "\t\t<attribute10></attribute10>\n" +
                    "\t\t<insert_time>2014-8-29 8:48:52</insert_time>\n" +
                    "\t\t<receive_system></receive_system>\n" +
                    "\t\t<stature>165</stature>\n" +
                    "\t\t<weight>60</weight>\n" +
                    "\t\t<recruit_channel>报纸广告</recruit_channel>\n" +
                    "\t\t<channel_name>广州日报</channel_name>\n" +
                    "\t\t<ancestral_home>广东省</ancestral_home>\n" +
                    "\t\t<family_phone></family_phone>\n" +
                    "\t\t<old_last_name></old_last_name>\n" +
                    "\t\t<registered_disabled_flag>否</registered_disabled_flag>\n" +
                    "\t</emp>\n" +
                    "\t<emp>\n" +
                    "\t\t<emp_id>33057946</emp_id>\n" +
                    "\t\t<person_id>145025</person_id>\n" +
                    "\t\t<emp_num>iA5EVDqs9ggADB/I6AYPrg==</emp_num>\n" +
                    "\t\t<last_name>h1Yg0NurR8JwPOXbz9q4Hw==</last_name>\n" +
                    "\t\t<first_name>0Uc90ObGmiimSQSYki6Vsg==</first_name>\n" +
                    "\t\t<sex>M</sex>\n" +
                    "\t\t<curr_org_id>2833</curr_org_id>\n" +
                    "\t\t<net_code>755A</net_code>\n" +
                    "\t\t<curr_org_name>755A.深圳区福田分部</curr_org_name>\n" +
                    "\t\t<org_code>755A</org_code>\n" +
                    "\t\t<curr_area>深圳区</curr_area>\n" +
                    "\t\t<position_id>9369</position_id>\n" +
                    "\t\t<position_name>仓管员</position_name>\n" +
                    "\t\t<position_code>DTOPJC043</position_code>\n" +
                    "\t\t<job_id>68</job_id>\n" +
                    "\t\t<job_name>基层员工</job_name>\n" +
                    "\t\t<postal_code></postal_code>\n" +
                    "\t\t<address></address>\n" +
                    "\t\t<mail_address>Alexchow@sf-express.com</mail_address>\n" +
                    "\t\t<mobile_phone></mobile_phone>\n" +
                    "\t\t<office_phone></office_phone>\n" +
                    "\t\t<job_date_from>2001-6-1</job_date_from>\n" +
                    "\t\t<sf_date>2001-6-1</sf_date>\n" +
                    "\t\t<cancel_date></cancel_date>\n" +
                    "\t\t<cancel_flag>N</cancel_flag>\n" +
                    "\t\t<date_from>2014-8-28</date_from>\n" +
                    "\t\t<last_zno>757A</last_zno>\n" +
                    "\t\t<last_org_name>757A.佛山区南海分部</last_org_name>\n" +
                    "\t\t<person_type>全日制员工</person_type>\n" +
                    "\t\t<emp_category>正式</emp_category>\n" +
                    "\t\t<office_addr>中国香港</office_addr>\n" +
                    "\t\t<edu_level></edu_level>\n" +
                    "\t\t<effective_date>2014-8-28</effective_date>\n" +
                    "\t\t<position_type>操作类</position_type>\n" +
                    "\t\t<position_group>营运</position_group>\n" +
                    "\t\t<position_attr>二线</position_attr>\n" +
                    "\t\t<speciality></speciality>\n" +
                    "\t\t<supervisor_number>000001</supervisor_number>\n" +
                    "\t\t<supervisor_name>张三UAT</supervisor_name>\n" +
                    "\t\t<emp_cate_code>CN_ZS</emp_cate_code>\n" +
                    "\t\t<ass_category>管理职务晋升</ass_category>\n" +
                    "\t\t<ass_reason>跨组织变动</ass_reason>\n" +
                    "\t\t<leaving_reason></leaving_reason>\n" +
                    "\t\t<vacation_start_date>2001-6-1</vacation_start_date>\n" +
                    "\t\t<date_of_birth>1969-4-9</date_of_birth>\n" +
                    "\t\t<cn_race>汉族</cn_race>\n" +
                    "\t\t<hukou_local>其它</hukou_local>\n" +
                    "\t\t<blood_type>AB</blood_type>\n" +
                    "\t\t<marital_status>已婚</marital_status>\n" +
                    "\t\t<probation_end_date></probation_end_date>\n" +
                    "\t\t<attribute1>中国香港</attribute1>\n" +
                    "\t\t<attribute5></attribute5>\n" +
                    "\t\t<attribute6></attribute6>\n" +
                    "\t\t<attribute7></attribute7>\n" +
                    "\t\t<attribute8></attribute8>\n" +
                    "\t\t<attribute9></attribute9>\n" +
                    "\t\t<attribute10></attribute10>\n" +
                    "\t\t<insert_time>2014-8-29 8:48:52</insert_time>\n" +
                    "\t\t<receive_system></receive_system>\n" +
                    "\t\t<stature>180</stature>\n" +
                    "\t\t<weight>60</weight>\n" +
                    "\t\t<recruit_channel></recruit_channel>\n" +
                    "\t\t<channel_name></channel_name>\n" +
                    "\t\t<ancestral_home>广东</ancestral_home>\n" +
                    "\t\t<family_phone></family_phone>\n" +
                    "\t\t<old_last_name></old_last_name>\n" +
                    "\t\t<registered_disabled_flag>否</registered_disabled_flag>\n" +
                    "\t</emp>\n" +
                    "</emps>");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HREmpXMLReader hrEmpXMLReader = new HREmpXMLReader();
        List<HrEmpInfo> hrEmpInfos = hrEmpXMLReader.readEmpXml(file, new HREmpXMLReader.OnSaveHandler() {
            @Override
            public void onSave(List<HrEmpInfo> hrEmpInfos) {
            }
        });
        assertTrue(hrEmpInfos.size() == 5);
        assertTrue(hrEmpInfos.get(0).getBatchNumber().equals("20140904091433"));
        assertTrue(hrEmpInfos.get(0).getDeptCode().equals("020MB"));
        assertTrue(hrEmpInfos.get(0).getSfDate().equals("2010-3-12"));
        DateTime dateTime = new DateTime(hrEmpInfos.get(0).getRegisterDt());
        assertTrue(dateTime.toString("yyyy-MM-dd")
                .equals("2010-03-12"));
        //        System.out.println(empCode);
        assertTrue(hrEmpInfos.get(0).getEmpCode().equals("184762"));
    }

}

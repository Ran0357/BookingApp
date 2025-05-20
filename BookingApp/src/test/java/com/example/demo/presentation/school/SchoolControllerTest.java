package com.example.demo.presentation.school;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.demo.application.service.school.SchoolAppService;
import com.example.demo.domain.model.FacilityType;

/*
 * 施設情報表示・予約 Controller Test
 */
@AutoConfigureMockMvc
@SpringBootTest
class SchoolControllerTest {
	
	List<FacilityType> facilityTypeList = new ArrayList<>();
	FacilityType facilityType1 = new FacilityType();
	FacilityType facilityType2 = new FacilityType();
	FacilityType facilityType3 = new FacilityType();
	FacilityUseForm facilityForm = new FacilityUseForm();
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private SchoolAppService schoolAppService;
	
	@BeforeEach
	void setUp() throws Exception {
		
		facilityType1.setId(1);
		facilityType1.setName("カラオケ");
		facilityType1.setCapacity(20);
		facilityTypeList.add(facilityType1);
		
		facilityType2.setId(2);
		facilityType2.setName("ジム");
		facilityType2.setCapacity(8);
		facilityTypeList.add(facilityType2);
		
		facilityType3.setId(3);
		facilityType3.setName("ブース");
		facilityType3.setCapacity(1);
		facilityTypeList.add(facilityType3);
		
		facilityForm.setFacilityId(1);
		facilityForm.setUseDate(LocalDate.of(2023, 10, 1));
		facilityForm.setStartTime(java.time.LocalTime.of(10, 0));
		facilityForm.setEndTime(java.time.LocalTime.of(12, 0));
		facilityForm.setNumberOfPeople(2);
		facilityForm.setFacilityName("カラオケ");
		
	}

	@WithMockUser(username = "user", roles = {"USER"})
	@Test
	@DisplayName("正常系：サイトタイプ一覧表示画面に遷移した時、サイトタイプを全件取得できている")
	void testFacilityTypeList() throws Exception {
		
		when(schoolAppService.findAllFacilityType()).thenReturn(facilityTypeList);
		
		// 検証 & 実行
		mockMvc.perform(get("/school/facilityTypes"))
				.andExpect(status().isOk())
				.andExpect(view().name("school/facilityTypes"))
				.andExpect(model().attribute("facilityTypeList", facilityTypeList));
	}
	
	@WithMockUser(username = "testuser", roles = {"USER"})
	@Test
	@DisplayName("正常系：サイトタイプIDが1のスケジュール表示画面に遷移した時、サイトタイプIDとサイトタイプ名が取得できている")
	void testScheduleByFacilityTypeId1() throws Exception {
		
		when(schoolAppService.findFacilityTypeName(1)).thenReturn(facilityType1.getName());
		
		// 検証 & 実行
		mockMvc.perform(get("/school/schedule")
				.param("facilityTypeId", "1"))
		.andExpect(status().isOk())
		.andExpect(view().name("school/schedule"))
		.andExpect(model().attribute("facilityTypeId", 1))
		.andExpect(model().attribute("facilityTypeName", "カラオケ"));
	}
	
	@WithMockUser(username = "testuser", roles = {"USER"})
	@Test
	@DisplayName("正常系：サイトタイプIDが1の宿泊情報入力画面に遷移した時、サイトタイプ名が取得できている")
	void testFacilityUseFormCatchFacilityTypeName() throws Exception {
		
		when(schoolAppService.findFacilityTypeName(1)).thenReturn("カラオケ");

		// 実行 & 検証
		MvcResult result = mockMvc.perform(get("/school/facilityInfo")
				.param("facilityId", "1")
				.param("useDate", "2023-10-01"))
				.andExpect(status().isOk())
				.andExpect(view().name("school/facilityInfo"))
				.andReturn();

		FacilityUseForm resultForm = (FacilityUseForm) result.getModelAndView().getModel().get("facilityUseForm");

		assertEquals("カラオケ", resultForm.getFacilityName());
	}

	
}

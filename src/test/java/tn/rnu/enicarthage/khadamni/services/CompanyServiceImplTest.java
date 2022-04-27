package tn.rnu.enicarthage.khadamni.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.rnu.enicarthage.khadamni.models.Company;
import tn.rnu.enicarthage.khadamni.repositories.CompanyRepository;
import tn.rnu.enicarthage.khadamni.services.interfaces.CompanyService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {
    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService = new CompanyServiceImpl();

    @Test
    @DisplayName("getCompanies(): Should return all companies.")
    void whenGetCompanies_thenReturnAllCompanies() {
        ArrayList<Company> companies = new ArrayList<>();
        companies.add(new Company(1L, "Name", "Address", "Description", "Contact Number", "Category", null, "Logo File Name", null, null));
        companies.add(new Company(2L, "Name", "Address", "Description", "Contact Number", "Category", null, "Logo File Name", null, null));

        Mockito.when(companyRepository.findAll()).thenReturn(companies);

        List<Company> companiesToTest = companyService.getCompanies();

        assertIterableEquals(companiesToTest, companies);
    }

    @Test
    @DisplayName("getCompanyById(): Should return the correct company when a valid id is provided.")
    void whenGetCompanyById_withValidId_thenReturnCompany() {
        Company company = new Company(1L, "Name", "Address", "Description", "Contact Number", "Category", null, "Logo File Name", null, null);

        Mockito.when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        Company companyToTest = companyService.getCompanyById(1L);

        assertEquals(companyToTest, company);
    }

    @Test
    @DisplayName("getCompanyById(): Should return null when an invalid id is provided.")
    void whenGetCompanyById_withInvalidId_thenReturnNull() {
        Mockito.when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        Company companyToTest = companyService.getCompanyById(1L);

        assertNull(companyToTest);
    }

    @Test
    @DisplayName("companyExists(): Should return true when a valid company's id is provided.")
    void whenCompanyExists_withValidCompanyId_thenReturnTrue() {
        Mockito.when(companyRepository.existsById(1L)).thenReturn(true);

        boolean result = companyService.companyExists(1L);

        assertTrue(result);
    }

    @Test
    @DisplayName("companyExists(): Should return false when an invalid company's id is provided.")
    void whenCompanyExists_withInvalidCompanyId_thenReturnFalse() {
        Mockito.when(companyRepository.existsById(1L)).thenReturn(false);

        boolean result = companyService.companyExists(1L);

        assertFalse(result);
    }

    @Test
    @DisplayName("addCompany(): Should return added company.")
    void whenAddCompany_thenReturnAddedCompany() {
        Company company = new Company(1L, "Name", "Address", "Description", "Contact Number", "Category", null, "Logo File Name", null, null);

        Mockito.when(companyRepository.save(company)).thenReturn(company);

        Company companyToTest = companyService.addCompany(company);

        Mockito.verify(companyRepository, Mockito.times(1)).save(company);
        assertEquals(companyToTest, company);
    }

    @Test
    @DisplayName("addCompany(): Should return updated company when a valid id is provided.")
    void whenUpdateCompany_withValidId_thenReturnUpdatedCompany() {
        Company company = new Company(1L, "Name", "Address", "Description", "Contact Number", "Category", null, "Logo File Name", null, null);
        Company updatedCompany = new Company(1L, "Updated Name", "Updated Address", "Updated Description", "Updated Contact Number", "Updated Category", null, "Updated Logo File Name", null, null);

        Mockito.when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        Company companyToTest = companyService.updateCompany(1L, updatedCompany);

        Mockito.verify(companyRepository, Mockito.times(1)).save(company);
        assertEquals(companyToTest, updatedCompany);
    }

    @Test
    @DisplayName("addCompany(): Should return null when an invalid id is provided.")
    void whenUpdateCompany_withInvalidId_thenReturnNull() {
        Mockito.when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        Company companyToTest = companyService.updateCompany(1L, new Company());

        assertNull(companyToTest);
    }

    @Test
    void deleteCompany() {
        companyService.deleteCompany(1L);

        Mockito.verify(companyRepository, Mockito.times(1)).deleteById(1L);
    }
}
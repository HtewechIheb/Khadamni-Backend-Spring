package tn.rnu.enicarthage.khadamni.services.interfaces;

import tn.rnu.enicarthage.khadamni.models.Company;

import java.util.List;

public interface CompanyService {
    List<Company> getCompanies();
    Company addCompany(Company company);
    Company getCompanyById(Long id);
    boolean companyExists(Long id);
    Company updateCompany(Long id, Company updatedCompany);
    void deleteCompany(Long id);
}

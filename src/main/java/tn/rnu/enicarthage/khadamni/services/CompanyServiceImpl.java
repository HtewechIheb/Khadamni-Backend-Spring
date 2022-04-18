package tn.rnu.enicarthage.khadamni.services;

import org.hibernate.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.rnu.enicarthage.khadamni.models.Company;
import tn.rnu.enicarthage.khadamni.repositories.CompanyRepository;
import tn.rnu.enicarthage.khadamni.services.interfaces.CompanyService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public List<Company> getCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Company addCompany(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public boolean companyExists(Long id) { return companyRepository.existsById(id); }

    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    @Override
    public Company updateCompany(Long id, Company updatedCompany) {
        Company companyToUpdate = companyRepository.findById(id).orElse(null);

        if(Objects.nonNull(companyToUpdate)){
            if(!StringHelper.isBlank(updatedCompany.getName())){
                companyToUpdate.setName(updatedCompany.getName());
            }
            if(!StringHelper.isBlank(updatedCompany.getAddress())){
                companyToUpdate.setAddress(updatedCompany.getAddress());
            }
            if(!StringHelper.isBlank(updatedCompany.getDescription())){
                companyToUpdate.setDescription(updatedCompany.getDescription());
            }
            if(!StringHelper.isBlank(updatedCompany.getContactNumber())){
                companyToUpdate.setContactNumber(updatedCompany.getContactNumber());
            }
            if(!StringHelper.isBlank(updatedCompany.getCategory())){
                companyToUpdate.setCategory(updatedCompany.getCategory());
            }
            if(!StringHelper.isBlank(updatedCompany.getLogoFileName())){
                companyToUpdate.setLogoFileName(updatedCompany.getLogoFileName());
            }
            if(Objects.nonNull(updatedCompany.getLogoFile()) && updatedCompany.getLogoFile().length > 0){
                companyToUpdate.setLogoFile(updatedCompany.getLogoFile());
            }

            companyRepository.save(companyToUpdate);
        }

        return companyToUpdate;
    }

    @Override
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}

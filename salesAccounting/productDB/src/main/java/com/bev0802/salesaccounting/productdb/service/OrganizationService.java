package com.bev0802.salesaccounting.productdb.service;

import com.bev0802.salesaccounting.productdb.model.Organization;
import com.bev0802.salesaccounting.productdb.repository.EmployeeRepository;
import com.bev0802.salesaccounting.productdb.repository.OrganizationRepository;
import com.bev0802.salesaccounting.productdb.repository.ProductRepository;
import com.bev0802.salesaccounting.productdb.repository.specification.OrganizationSpecifications;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления организациями в базе данных.
 */
@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public void deleteOrganization(Long id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found with id " + id));
        // Delete all associated entities
        productRepository.deleteAllByOrganization(organization);
        employeeRepository.deleteAllByOrganization(organization);
        // Finally, delete the organization
        organizationRepository.delete(organization);
    }

    // Регистрация организации
    public Organization registerOrganization(Organization organization) {
        // Проверка на существование организации с таким же ИНН
        organizationRepository.findByInn(organization.getInn())
                .ifPresent(o -> {
                    throw new DataIntegrityViolationException("Организация с ИНН " + organization.getInn() + " уже зарегистрирована.");
                });

        // Проверка на существование организации с таким же email
        organizationRepository.findByEmail(organization.getEmail())
                .ifPresent(o -> {
                    throw new DataIntegrityViolationException("Организация с email " + organization.getEmail() + " уже зарегистрирована.");
                });

        // Если проверки пройдены, сохраняем новую организацию
        return organizationRepository.save(organization);
    }

    // Аутентификация организации
    public Optional<Organization> authenticate(String inn, String password) {
        return organizationRepository.findByInnAndPassword(inn, password);
    }

    public Organization updateOrganization(Long id, Organization organizationDetails) {
        // Поиск организации по ID
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found with id " + id));

        // Обновление полей организации
        organization.setName(organizationDetails.getName());
        organization.setInn(organizationDetails.getInn());
        organization.setKpp(organizationDetails.getKpp());
        organization.setAddress(organizationDetails.getAddress());
        organization.setEmail(organizationDetails.getEmail());
        organization.setPassword(organizationDetails.getPassword());

        // Сохранение обновленной организации в базе данных
        return organizationRepository.save(organization);
    }

    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }

    public List<Organization> searchByNameOrInn(String name, String inn) {
        Specification<Organization> spec = Specification.where(null);

        if (name != null && !name.isEmpty()) {
            spec = spec.and(OrganizationSpecifications.nameContains(name));
        }

        if (inn != null && !inn.isEmpty()) {
            spec = spec.and(OrganizationSpecifications.innContains(inn));
        }

        return organizationRepository.findAll(spec);
    }

    public Optional<Organization> findById(Long id) {
        return organizationRepository.findById(id);
    }
}


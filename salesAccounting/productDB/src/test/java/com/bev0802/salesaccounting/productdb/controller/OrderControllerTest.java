package com.bev0802.salesaccounting.productdb.controller;

import com.bev0802.salesaccounting.productdb.model.Employee;
import com.bev0802.salesaccounting.productdb.model.Organization;
import com.bev0802.salesaccounting.productdb.model.Product;
import com.bev0802.salesaccounting.productdb.service.EmployeeService;
import com.bev0802.salesaccounting.productdb.service.OrderService;
import com.bev0802.salesaccounting.productdb.service.OrganizationService;
import com.bev0802.salesaccounting.productdb.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    @MockBean
    private ProductService productService;

    @MockBean
    private OrganizationService organizationService;

    @MockBean
    private EmployeeService employeeService;

    @Test
    public void testAddProductToOrder() throws Exception {
        Long organizationId = 1L;
        Long employeeId = 1L;
        Long productId = 10L;
        BigDecimal quantity = new BigDecimal("2");

        Product product = new Product();
        product.setId(productId);
        product.setPrice(new BigDecimal("100"));

        Organization organization = new Organization();
        organization.setId(organizationId);

        Employee employee = new Employee();
        employee.setId(employeeId);

        Mockito.when(productService.getProductById(productId)).thenReturn(product);
        Mockito.when(organizationService.findById(organizationId)).thenReturn(Optional.of(organization));
        Mockito.when(employeeService.findById(employeeId)).thenReturn(Optional.of(employee));

        mockMvc.perform(post("/orders/addProduct")
                        .param("organizationId", organizationId.toString())
                        .param("employeeId", employeeId.toString())
                        .param("productId", productId.toString())
                        .param("quantity", quantity.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value("200"))
                .andExpect(jsonPath("$.items[0].product.id").value(productId));
    }
}
package kitchenpos;

import kitchenpos.table.domain.TableOrderStatusChecker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class ApplicationTest {
    @MockBean
    TableOrderStatusChecker tableOrderStatusChecker;

    @Test
    void contextLoads() {
    }
}

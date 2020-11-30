package com.vanni.community;

import com.vanni.community.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTest {

    @Autowired
    private SensitiveFilter filter;

    @Test
    public void testSensitiveFilter()
    {
        String text = "今晚开波, 今晚开票, 今晚十点准时开台";
        String filterString = this.filter.filter(text);
        System.out.println(filterString);
    }
}

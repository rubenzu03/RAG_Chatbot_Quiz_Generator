package com.rubenzu03.rag_chatbot.application.service;

import com.rubenzu03.rag_chatbot.infrastructure.ragmodules.preretrieve.QueryTransformerModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.rag.Query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransformQueryServiceTest {

    private TransformQueryService service;

    @Mock
    private QueryTransformerModule queryTransformer;

    @BeforeEach
    void setUp() {
        service = new TransformQueryService(queryTransformer);
    }

    @Test
    void testTransformQueryWithValidQuery() {
        Query originalQuery = Query.builder().text("What is RAG?").build();
        Query transformedQuery = Query.builder().text("What is Retrieval Augmented Generation?").build();
        when(queryTransformer.transformQuery(anyString(), anyString()))
                .thenReturn(transformedQuery);

        Query result = service.transformQuery(originalQuery, "user123");
        assertThat(result.text()).isEqualTo("What is Retrieval Augmented Generation?");
    }

    @Test
    void testTransformQueryPreservesStructure() {
        Query originalQuery = Query.builder()
                .text("How does clustering work?")
                .build();
        Query transformedQuery = Query.builder()
                .text("Explain clustering algorithms")
                .build();

        when(queryTransformer.transformQuery(anyString(), anyString()))
                .thenReturn(transformedQuery);

        Query result = service.transformQuery(originalQuery, "user456");

        assertThat(result).isNotNull();
        assertThat(result.text()).isNotEmpty();
    }

    @Test
    void testTransformQueryWithDifferentUserId() {
        Query query = Query.builder().text("test query").build();
        Query expected = Query.builder().text("transformed query").build();

        when(queryTransformer.transformQuery(anyString(), anyString()))
                .thenReturn(expected);

        Query result = service.transformQuery(query, "different-user");

        assertThat(result.text()).isEqualTo("transformed query");
    }

    @Test
    void testTransformQueryWithComplexQuery() {
        Query complexQuery = Query.builder()
                .text("How can I implement machine learning models with Python?")
                .build();
        Query simplifiedQuery = Query.builder()
                .text("Implement ML models Python")
                .build();

        when(queryTransformer.transformQuery(anyString(), anyString()))
                .thenReturn(simplifiedQuery);

        Query result = service.transformQuery(complexQuery, "user789");

        assertThat(result.text()).isNotNull();
        assertThat(result.text()).contains("ML", "models");
    }
}

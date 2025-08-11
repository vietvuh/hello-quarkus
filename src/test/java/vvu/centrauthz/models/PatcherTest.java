package vvu.centrauthz.models;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class PatcherTest {

    @Test
    void builder() {
        assertThrowsExactly(IllegalArgumentException.class,
                () -> Patcher
                        .<String>builder()
                        .fields(List.of("name")).build());
        assertThrowsExactly(IllegalArgumentException.class,
                () -> Patcher.<String>builder().data("test").build());
        assertDoesNotThrow(() -> Patcher
                .<String>builder()
                .fields(List.of("name"))
                .data("test").build());
    }

    @Test
    void having_hasField_doIt() {
        var field1 = UUID.randomUUID().toString().split("-")[0];
        var field2 = UUID.randomUUID().toString().split("-")[0];
        Patcher<String> patcher =
            Patcher
                .<String>builder()
                .fields(List.of(field1, field2))
                .data(UUID.randomUUID().toString().split("-")[0])
                .build();

        Consumer<String> consumer =Mockito.mock(Consumer.class);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.doNothing().when(consumer).accept(captor.capture());
        patcher.having(field1, consumer);
        Mockito.verify(consumer, Mockito.only()).accept(Mockito.any());
        assertEquals(patcher.data(), captor.getValue());
    }

    @Test
    void having_hasNoField_NotDoIt() {
        var field1 = UUID.randomUUID().toString().split("-")[0];
        var field2 = UUID.randomUUID().toString().split("-")[0];
        var field3 = UUID.randomUUID().toString().split("-")[0];

        Patcher<String> patcher = Patcher
            .<String>builder()
            .fields(List.of(field1, field2))
            .data(UUID.randomUUID().toString())
            .build();
        Consumer<String> consumer =Mockito.mock(Consumer.class);
        patcher.having(field3, consumer);
        Mockito.verify(consumer, Mockito.never()).accept(Mockito.any());
    }

}
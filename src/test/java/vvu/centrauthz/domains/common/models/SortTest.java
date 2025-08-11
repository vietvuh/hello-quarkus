package vvu.centrauthz.domains.common.models;

import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class SortTest {

    @Test
    void from_whenInputIsEmpty_IllegalArgumentException() {
        assertThrowsExactly(IllegalArgumentException.class, () -> Sort.from(null));
        assertThrowsExactly(IllegalArgumentException.class, () -> Sort.from(""));
    }

    @Test
    void from_whenInputIsIllegal_IllegalArgumentException() {
        assertThrowsExactly(IllegalArgumentException.class, () -> Sort.from("name:ZZZ"));
    }

    @Test
    void from_whenFieldEmpty_IllegalArgumentException() {
        assertThrowsExactly(IllegalArgumentException.class, () -> Sort.from(":"));
    }

    @Test
    void from_whenFieldWithoutDirection_ASC() {
        var sort = Sort.from("name:");
        assertEquals("name", sort.field());
        assertEquals(SortDirection.ASC, sort.direction());

        sort = Sort.from("name");
        assertEquals("name", sort.field());
        assertEquals(SortDirection.ASC, sort.direction());
    }

    @Test
    void from_whenFieldWithDirection_Direction() {
        var sort = Sort.from("name:asc");
        assertEquals("name", sort.field());
        assertEquals(SortDirection.ASC, sort.direction());

        sort = Sort.from("name:Asc");
        assertEquals("name", sort.field());
        assertEquals(SortDirection.ASC, sort.direction());

        sort = Sort.from("name:desc");
        assertEquals("name", sort.field());
        assertEquals(SortDirection.DESC, sort.direction());

        sort = Sort.from("name:DESC");
        assertEquals("name", sort.field());
        assertEquals(SortDirection.DESC, sort.direction());

        sort = Sort.from("name");
        assertEquals("name", sort.field());
        assertEquals(SortDirection.ASC, sort.direction());
    }

    @Test
    void multiple() {
        var values = "name:asc,age:desc,createdDate:asc";
        var sortList = Sort.list(values);
        var map = sortList.stream().collect(Collectors.toMap(Sort::field, Sort::direction));
        assertEquals(map.get("name"), SortDirection.ASC);
        assertEquals(map.get("age"), SortDirection.DESC);
        assertEquals(map.get("createdDate"), SortDirection.ASC);
    }
}
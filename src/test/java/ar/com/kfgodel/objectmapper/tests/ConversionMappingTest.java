package ar.com.kfgodel.objectmapper.tests;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import ar.com.dgarcia.javaspec.api.TestContext;
import ar.com.dgarcia.objectmapper.api.TypeMapper;
import ar.com.dgarcia.objectmapper.impl.EnsembleMapper;
import ar.com.dgarcia.objectmapper.impl.JacksonMapper;
import ar.com.kfgodel.objectmapper.tests.mapper.TypicalObjectMapper;
import ar.com.kfgodel.objectmapper.tests.testObjects.TypicalObject;
import org.junit.runner.RunWith;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This type verifies the performance of jackson map conversions
 * 
 * Created by kfgodel on 18/11/14.
 */
@RunWith(JavaSpecRunner.class)
public class ConversionMappingTest extends JavaSpec<TestContext> {
    @Override
    public void define() {

        describe("from object to map", ()->{

            it("should be complete with a custom mapper",()->{
                testDisassemblyFor(TypicalObjectMapper.create());
            });
            it("should be complete with jackson mapper",()->{
                testDisassemblyFor(JacksonMapper.create());
            });
            it("should be complete with ensemble mapper",()->{
                testDisassemblyFor(EnsembleMapper.create());
            });

        });

        describe("from map to object", () -> {

            it("should be complete with a custom mapper", () -> {

            });
            it("should be complete with jackson mapper", () -> {

            });
            it("should be complete with implementation mapper", () -> {

            });
        });


    }

    private void testDisassemblyFor(TypeMapper customMapper) {
        TypicalObject typicalObject = TypicalObject.create();
        typicalObject.initializeRootTestData();

        Map<String, Object> converted = customMapper.toMap(typicalObject);

        assertThat(converted).isEqualTo(TypicalObject.createRootTestMap());
    }
}
package ar.com.kfgodel.objectmapper.tests;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import ar.com.dgarcia.javaspec.api.TestContext;
import ar.com.dgarcia.objectmapper.impl.DiamondMapper;
import ar.com.dgarcia.objectmapper.impl.JacksonMapper;
import ar.com.dgarcia.objectmapper.api.TypeMapper;
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
                TypicalObject typicalObject = TypicalObject.create();
                typicalObject.initializeRootTestData();

                TypeMapper customMapper = TypicalObjectMapper.create();
                Map<String, Object> converted = customMapper.toMap(typicalObject);

                assertThat(converted).isEqualTo(TypicalObject.createRootTestMap());
            });
            it("should be complete with jackson mapper",()->{
                TypicalObject typicalObject = TypicalObject.create();
                typicalObject.initializeRootTestData();

                TypeMapper jacksonMapper = JacksonMapper.create();
                Map<String, Object> converted = jacksonMapper.toMap(typicalObject);

                assertThat(converted).isEqualTo(TypicalObject.createRootTestMap());
            });
            it("should be complete with implementation mapper",()->{
                TypicalObject typicalObject = TypicalObject.create();
                typicalObject.initializeRootTestData();

                TypeMapper jacksonMapper = DiamondMapper.create();
                Map<String, Object> converted = jacksonMapper.toMap(typicalObject);

                assertThat(converted).isEqualTo(TypicalObject.createRootTestMap());

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
}
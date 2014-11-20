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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This type verifies the minimum basic mapper
 * Created by kfgodel on 18/11/14.
 */
@RunWith(JavaSpecRunner.class)
public class ConversionPerformanceIT extends JavaSpec<TestContext> {
    public static Logger LOG = LoggerFactory.getLogger(ConversionPerformanceIT.class);

    @Override
    public void define() {
        describe("measuring conversion times", () -> {

            describe("from object to map", () -> {
                it("warm up",()->{
                    TypicalObject typicalObject = TypicalObject.create();
                    typicalObject.initializeRootTestData();

                    TypicalObjectMapper testedMapper = TypicalObjectMapper.create();
                    Map<String, Object> converted = null;
                    for (int i = 0; i < 1_000_000; i++) {
                        converted = testedMapper.toMap(typicalObject);
                    }
                    assertThat(converted).isEqualTo(TypicalObject.createRootTestMap());
                });
                it("native mapper",()->{
                    runTestsOn(TypicalObjectMapper.create());
                });
                it("implementation mapper",()->{
                    runTestsOn(DiamondMapper.create());
                });
                it("jackson mapper",()->{
                    runTestsOn(JacksonMapper.create());
                });
            });


            describe("from map to object", () -> {
                it("native mapper",()->{

                });
                it("jackson mapper",()->{

                });
                it("implementation mapper",()->{

                });
            });
        });
    }

    private void runTestsOn(TypeMapper testedMapper) {
        TypicalObject typicalObject = TypicalObject.create();
        typicalObject.initializeRootTestData();


        long started = System.nanoTime();
        Map<String, Object> converted = null;
        for (int i = 0; i < 1_000_000; i++) {
            converted = testedMapper.toMap(typicalObject);
        }
        long ended = System.nanoTime();
        LOG.info("{}: {} ms", testedMapper.getClass().getSimpleName() , TimeUnit.NANOSECONDS.toMillis(ended - started));
        assertThat(converted).isEqualTo(TypicalObject.createRootTestMap());
    }
}
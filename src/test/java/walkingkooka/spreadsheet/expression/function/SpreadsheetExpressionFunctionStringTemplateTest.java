/*
 * Copyright 2022 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.spreadsheet.expression.function;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.template.TemplateValueName;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetExpressionFunctionStringTemplateTest extends SpreadsheetExpressionFunctionStringTestCase<SpreadsheetExpressionFunctionStringTemplate> {

    @Override
    public void testSetParametersSame() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testParametersIfConvertTypeNotObject() {
        throw new UnsupportedOperationException();
    }

    // apply............................................................................................................

    @Test
    public void testApplyWithPlainTextTemplateString() {
        final String text = "Hello World 123";

        this.applyAndCheck2(
            Lists.of(text),
            text
        );
    }

    @Test
    public void testApplyWithTemplateMissingParameterValueFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetExpressionFunctionStringTemplate.INSTANCE.apply(
                Lists.of(
                    "Plain text template",
                    TemplateValueName.with("TemplateValueName1")
                ),
                this.createContext()
            )
        );

        this.checkEquals(
            "Named parameter 1: missing value",
            thrown.getMessage()
        );
    }

    @Test
    public void testApplyWithTemplateMissingParameterValueFails2() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetExpressionFunctionStringTemplate.INSTANCE.apply(
                Lists.of(
                    "Plain text template",
                    TemplateValueName.with("TemplateValueName1"),
                    "TemplateValue1",
                    TemplateValueName.with("TemplateValueName2")
                ),
                this.createContext()
            )
        );

        this.checkEquals(
            "Named parameter 2: missing value",
            thrown.getMessage()
        );
    }

    @Test
    public void testApplyWithTemplateMissingParameterValueFails3() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetExpressionFunctionStringTemplate.INSTANCE.apply(
                Lists.of(
                    "Plain text template",
                    TemplateValueName.with("TemplateValueName1"),
                    "TemplateValue1",
                    TemplateValueName.with("TemplateValueName2"),
                    "TemplateValue2",
                    TemplateValueName.with("TemplateValueName3")
                ),
                this.createContext()
            )
        );

        this.checkEquals(
            "Named parameter 3: missing value",
            thrown.getMessage()
        );
    }

    @Test
    public void testApplyWithTemplateWithNamedParameter() {
        this.applyAndCheck2(
            Lists.of(
                "Hello ${Who}",
                TemplateValueName.with("Who"),
                "WORLD"
            ),
            "Hello WORLD"
        );
    }

    @Test
    public void testApplyWithTemplateWithNamedParameter2() {
        this.applyAndCheck2(
            Lists.of(
                "Hello ${Who1} and ${Who2}",
                TemplateValueName.with("Who1"),
                "WORLD",
                TemplateValueName.with("Who2"),
                "AUSTRALIA"
            ),
            "Hello WORLD and AUSTRALIA"
        );
    }

    @Override
    public SpreadsheetExpressionFunctionStringTemplate createBiFunction() {
        return SpreadsheetExpressionFunctionStringTemplate.INSTANCE;
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }

    @Override
    public Class<SpreadsheetExpressionFunctionStringTemplate> type() {
        return SpreadsheetExpressionFunctionStringTemplate.class;
    }
}

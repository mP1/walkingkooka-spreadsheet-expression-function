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
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.Url;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetExpressionFunctionHyperlinkTest extends SpreadsheetExpressionFunctionTestCase<SpreadsheetExpressionFunctionHyperlink, AbsoluteUrl> {

    @Test
    public void testUnknownTypeFail() {
        assertThrows(
                ClassCastException.class,
                () -> this.apply2("???")
        );
    }

    @Test
    public void testReference() {
        this.applyAndCheck2(
                Lists.of(REFERENCE),
                Url.parseAbsolute("https://example.com/path789/1234/Untitled5678/cell/" + REFERENCE)
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBiFunction(), "hyperlink");
    }

    @Override
    public SpreadsheetExpressionFunctionHyperlink createBiFunction() {
        return SpreadsheetExpressionFunctionHyperlink.INSTANCE;
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }

    @Override
    public Class<SpreadsheetExpressionFunctionHyperlink> type() {
        return SpreadsheetExpressionFunctionHyperlink.class;
    }

    @Override
    public String typeNamePrefix() {
        return SpreadsheetExpressionFunction.class.getSimpleName();
    }
}

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
import walkingkooka.spreadsheet.expression.FakeSpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

public final class SpreadsheetExpressionFunctionSpreadsheetMetadataGetTest extends SpreadsheetExpressionFunctionTestCase<SpreadsheetExpressionFunctionSpreadsheetMetadataGet, Object>
        implements SpreadsheetMetadataTesting {

    @Test
    public void testApplyPropertyPresent() {
        this.applyAndCheck(
                Lists.of(
                        SpreadsheetMetadataPropertyName.LOCALE,
                        "missing!!!"
                ),
                METADATA_EN_AU.getOrFail(SpreadsheetMetadataPropertyName.LOCALE)
        );
    }

    @Test
    public void testApplyPropertyMissing() {
        final SpreadsheetMetadataPropertyName<?> property = SpreadsheetMetadataPropertyName.HIDE_ZERO_VALUES;

        this.checkEquals(
                null,
                METADATA_EN_AU.get(property)
                        .orElse(null)
        );

        final Object defaultValue = "Missing!!!";

        this.applyAndCheck(
                Lists.of(
                        SpreadsheetMetadataPropertyName.SPREADSHEET_NAME,
                        defaultValue
                ),
                defaultValue
        );
    }

    @Override
    public SpreadsheetExpressionFunctionSpreadsheetMetadataGet createBiFunction() {
        return SpreadsheetExpressionFunctionSpreadsheetMetadataGet.INSTANCE;
    }

    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
        return new FakeSpreadsheetExpressionEvaluationContext() {
            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return METADATA_EN_AU;
            }
        };
    }

    @Override
    public int minimumParameterCount() {
        return 2;
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createBiFunction(),
                "spreadsheetMetadataGet"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExpressionFunctionSpreadsheetMetadataGet> type() {
        return SpreadsheetExpressionFunctionSpreadsheetMetadataGet.class;
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}

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
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

import java.util.Optional;

public final class SpreadsheetExpressionFunctionSpreadsheetMetadataValueGetTest extends SpreadsheetExpressionFunctionSpreadsheetMetadataValueTestCase<SpreadsheetExpressionFunctionSpreadsheetMetadataValueGet, Object>
    implements SpreadsheetMetadataTesting {

    @Test
    public void testApplyPropertyPresent() {
        this.applyAndCheck(
            Lists.of(
                SpreadsheetMetadataPropertyName.LOCALE,
                "missing!!!"
            ),
            SPREADSHEET_METADATA.getOrFail(SpreadsheetMetadataPropertyName.LOCALE)
        );
    }

    @Test
    public void testApplySpreadsheetMetadataAndPropertyPresent() {
        this.applyAndCheck(
            this.createBiFunction(),
            Lists.of(
                SPREADSHEET_METADATA,
                SpreadsheetMetadataPropertyName.LOCALE,
                "missing!!!"
            ),
            new FakeSpreadsheetExpressionEvaluationContext() {

                @Override
                public Optional<SpreadsheetMetadata> loadMetadata(final SpreadsheetId id) {
                    return Optional.ofNullable(
                        SPREADSHEET_ID.equals(id) ?
                            SPREADSHEET_METADATA :
                            null
                    );
                }
            },
            SPREADSHEET_METADATA.getOrFail(SpreadsheetMetadataPropertyName.LOCALE)
        );
    }

    @Test
    public void testApplyPropertyMissing() {
        final SpreadsheetMetadataPropertyName<?> property = SpreadsheetMetadataPropertyName.HIDE_ZERO_VALUES;

        this.checkEquals(
            null,
            SPREADSHEET_METADATA.get(property)
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
    public SpreadsheetExpressionFunctionSpreadsheetMetadataValueGet createBiFunction() {
        return SpreadsheetExpressionFunctionSpreadsheetMetadataValueGet.INSTANCE;
    }

    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
        return new FakeSpreadsheetExpressionEvaluationContext() {
            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return SPREADSHEET_METADATA;
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
            "getSpreadsheetMetadataValue"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExpressionFunctionSpreadsheetMetadataValueGet> type() {
        return SpreadsheetExpressionFunctionSpreadsheetMetadataValueGet.class;
    }
}

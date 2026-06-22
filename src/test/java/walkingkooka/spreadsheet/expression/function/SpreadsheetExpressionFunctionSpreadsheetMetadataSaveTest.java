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

public final class SpreadsheetExpressionFunctionSpreadsheetMetadataSaveTest extends SpreadsheetExpressionFunctionTestCase<SpreadsheetExpressionFunctionSpreadsheetMetadataSave, SpreadsheetMetadata>
    implements SpreadsheetMetadataTesting {

    private final static SpreadsheetId SPREADSHEET_ID = SpreadsheetId.with(1);

    private final static SpreadsheetMetadata UNSAVED_METADATA = METADATA_EN_AU;

    private final static SpreadsheetMetadata SAVED_METADATA = UNSAVED_METADATA.set(
        SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
        SPREADSHEET_ID
    );

    @Test
    public void testApply() {
        this.applyAndCheck(
            Lists.of(UNSAVED_METADATA),
            SAVED_METADATA
        );
    }

    @Override
    public SpreadsheetExpressionFunctionSpreadsheetMetadataSave createBiFunction() {
        return SpreadsheetExpressionFunctionSpreadsheetMetadataSave.INSTANCE;
    }

    @Override
    public SpreadsheetExpressionEvaluationContext createContext() {
        return new FakeSpreadsheetExpressionEvaluationContext() {

            @Override
            public SpreadsheetMetadata saveMetadata(final SpreadsheetMetadata metadata) {
                checkEquals(
                    UNSAVED_METADATA,
                    metadata
                );

                return SAVED_METADATA;
            }
        };
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBiFunction(),
            "saveSpreadsheetMetadata"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExpressionFunctionSpreadsheetMetadataSave> type() {
        return SpreadsheetExpressionFunctionSpreadsheetMetadataSave.class;
    }
}

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

import walkingkooka.NeverError;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.expression.SpreadsheetExpressionEvaluationContext;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Arrays;
import java.util.Optional;

/**
 * An enum value for each type info parameter for the "cell" function.
 */
enum SpreadsheetExpressionFunctionObjectCellTypeInfo {
    ADDRESS {
        @Override
        Object value(final SpreadsheetCellReference reference,
                     final SpreadsheetCell cell,
                     final SpreadsheetExpressionEvaluationContext context) {
            return reference;
        }
    },
    COL {
        @Override
        Object value(final SpreadsheetCellReference reference,
                     final SpreadsheetCell cell,
                     final SpreadsheetExpressionEvaluationContext context) {
            return context.expressionNumberKind()
                .create(
                    reference.toCell()
                        .column()
                        .value()
                );
        }
    },

    COLOR {
        @Override
        Object value(final SpreadsheetCellReference reference,
                     final SpreadsheetCell cell,
                     final SpreadsheetExpressionEvaluationContext context) {
            throw new UnsupportedOperationException();
        }
    },

    // returns the value of the upper-left cell in reference. Formulas are not returned. Instead, the result of the formula is returned.
    CONTENTS {
        @Override
        Object value(final SpreadsheetCellReference reference,
                     final SpreadsheetCell cell,
                     final SpreadsheetExpressionEvaluationContext context) {
            final Optional<SpreadsheetCell> loaded = context.loadCell(reference);
            return loaded.isPresent() ?
                loaded.get()
                    .formattedValue()
                    .get()
                    .text() :
                "0"; // cell absent use 0
        }
    },

    FILENAME {
        @Override
        Object value(final SpreadsheetCellReference reference,
                     final SpreadsheetCell cell,
                     final SpreadsheetExpressionEvaluationContext context) {
            return ""; // always empty.
        }
    },

    FORMAT {
        @Override
        Object value(final SpreadsheetCellReference reference,
                     final SpreadsheetCell cell,
                     final SpreadsheetExpressionEvaluationContext context) {
            throw new UnsupportedOperationException();
        }
    },

    PARENTHESIS {
        @Override
        Object value(final SpreadsheetCellReference reference,
                     final SpreadsheetCell cell,
                     final SpreadsheetExpressionEvaluationContext context) {
            throw new UnsupportedOperationException();
        }
    },

    PREFIX {
        @Override
        Object value(final SpreadsheetCellReference reference,
                     final SpreadsheetCell cell,
                     final SpreadsheetExpressionEvaluationContext context) {
            String value = "";

            if (cell.formattedValue().isPresent()) {
                final Optional<TextAlign> maybeTextAlign = cell.style()
                    .get(TextStylePropertyName.TEXT_ALIGN);
                if (maybeTextAlign.isPresent()) {

                    final TextAlign textAlign = maybeTextAlign.get();
                    switch (textAlign) {
                        case LEFT:
                            value = "'";
                            break;
                        case RIGHT:
                            value = "\"";
                            break;
                        case CENTER:
                            value = "^";
                            break;
                        case JUSTIFY:
                            break;
                        default:
                            NeverError.unhandledCase(textAlign, TextAlign.values());
                            break;
                    }
                }
            }

            return value;
        }
    },

    PROTECT {
        @Override
        Object value(final SpreadsheetCellReference reference,
                     final SpreadsheetCell cell,
                     final SpreadsheetExpressionEvaluationContext context) {
            throw new UnsupportedOperationException();
        }
    },

    ROW {
        @Override
        Object value(final SpreadsheetCellReference reference,
                     final SpreadsheetCell cell,
                     final SpreadsheetExpressionEvaluationContext context) {
            return context.expressionNumberKind()
                .create(
                    reference
                        .toCell()
                        .row()
                        .value()
                );
        }
    },

    // returns a text value that corresponds to the type of data in the first cell in reference:
    // "b" for blank when the cell is empty,
    // "l"  for label if the cell contains a text constant, and
    // "v" for value if the cell contains anything else.
    TYPE {
        @Override
        Object value(final SpreadsheetCellReference reference,
                     final SpreadsheetCell cell,
                     final SpreadsheetExpressionEvaluationContext context) {
            throw new UnsupportedOperationException();
        }
    },

    WIDTH {
        @Override
        Object value(final SpreadsheetCellReference reference,
                     final SpreadsheetCell cell,
                     final SpreadsheetExpressionEvaluationContext context) {
            throw new UnsupportedOperationException();
        }
    };

    abstract Object value(final SpreadsheetCellReference reference,
                          final SpreadsheetCell cell,
                          final SpreadsheetExpressionEvaluationContext context);

    static SpreadsheetExpressionFunctionObjectCellTypeInfo typeInfo(final String typeInfo) {
        return Arrays.stream(values())
            .filter(v -> v.name().toLowerCase().equals(typeInfo))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown typeInfo " + CharSequences.quoteAndEscape(typeInfo)));
    }
}

[![Build Status](https://github.com/mP1/walkingkooka-spreadsheet-expression-function/actions/workflows/build.yaml/badge.svg)](https://github.com/mP1/walkingkooka-spreadsheet-expression-function/actions/workflows/build.yaml/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/mP1/walkingkooka-spreadsheet-expression-function/badge.svg?branch=master)](https://coveralls.io/github/mP1/walkingkooka-spreadsheet-expression-function?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/mP1/walkingkooka-spreadsheet-expression-function.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-spreadsheet-expression-function/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/mP1/walkingkooka-spreadsheet-expression-function.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-spreadsheet-expression-function/alerts/)
[![J2CL compatible](https://img.shields.io/badge/J2CL-compatible-brightgreen.svg)](https://github.com/mP1/j2cl-central)



# walkingkooka-spreadsheet-expression-function
A collection of ExpressionFunction(s) that require a spreadsheet



## Available functions

- address(rowNum, colNum, abs(
  num)) [TODO referenceStyle=r1c1](https://github.com/mP1/walkingkooka-spreadsheet-expression-function/issues/45) [TODO sheet](https://github.com/mP1/walkingkooka-spreadsheet-expression-function/issues/46)
- average(),
- averageIf(),
- cell(typeInfo=address, col, contents, filename, prefix,
  row) [TODO typeinfos](https://github.com/mP1/walkingkooka-spreadsheet-expression-function/issues/26)
- cellValue()
- column (cellReference?)
- columns (cellReference/range)
- count(),
- countIf(),
- error.type(object)
- formulatext(cellReference)
- hyperlink(cellReference)
- indirect(string)
- isBlank(object)
- isErr(object)
- isError(object)
- isFormula(cellReference/Range)
- isNa(object)
- isRef(object)
- lambda()
- max(),
- maxIf(),
- min(),
- minIf(),
- na()
- numberValue()
- offset(cellReference, rows, columns, height?, width?)
- row (cellReference?)
- rows (cellReference/range)
- sum(),
- sumIf()
- type(object)


Many more functions are outstanding and remain [TODO](https://github.com/mP1/walkingkooka-spreadsheet-expression-function/issues).
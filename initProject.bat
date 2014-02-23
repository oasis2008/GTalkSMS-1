@echo off
set ABSDIR=external-libs\ActionBarSherlock\library

if not exist %ABSDIR% (
    git submodule update --init --recursive || pause & exit 1
) else (
    echo Project already initialized
)

pause
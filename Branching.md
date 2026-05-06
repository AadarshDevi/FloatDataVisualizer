# Branches

```
main (Production)
├── hotfix/             <-- Emergency fixes directly for main
│
└── dev (Development)   <-- The integration hub for all work
    ├── release/        <-- Final polish/docs before merging to main
    │
    ├── feat/           <-- New features & script modules
    │
    ├── fix/            <-- Routine bug fixes during dev
    │
    └── lab/            <-- Risky experiments & hardware testing
```

## Regular Coding

```
dev --> new branch
```

If the code is:

1. feature: adding a new feature
2. fix: fixing a problem
3. lab: testing crazy stuff

Examples:

1. new feature - send data to server: new branch > `feat/server_send`
2. a fix - logging fix: new branch > `fix/logging/launcher`

## Notes

branches `feat/`, `fix/`, `lab/`, `release/`, and `hotfix/` are created when needed
these branches are temps.

## Permanent Branches

```
main --> dev
```
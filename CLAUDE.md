# QREZZY — Claude Code Instructions

@AGENTS.md

## Claude-specific workflow

For non-trivial tasks:

1. Inspect the relevant code before editing.
2. Search for analogous implementations in the repository.
3. Use Plan Mode when the task affects multiple files, architecture, persistence, navigation, or
   public behavior.
4. Keep the implementation focused on the requested scope.
5. Run the relevant validation commands defined in `AGENTS.md`.
6. Review the final diff before declaring the task complete.

## Permissions

Do not request broader permissions than necessary.

Prefer read-only exploration before modification.

Ask for approval before destructive or high-impact operations, including:

- deleting files,
- resetting or rewriting Git history,
- destructive database operations,
- changing signing or secret configuration,
- broad dependency upgrades,
- force-pushing or pushing changes.

## Subagents

Use subagents when independent analysis can be performed in parallel, for example:

- architecture review,
- test coverage analysis,
- Compose review,
- coroutine/Flow review,
- persistence review.

Do not use subagents unnecessarily for small, localized changes.

The main agent remains responsible for validating their findings before acting on them.

## Scope

Do not perform unrelated refactors or cleanup unless explicitly requested.

If repository evidence conflicts with a generic Android recommendation, follow the project rules in
`AGENTS.md`.
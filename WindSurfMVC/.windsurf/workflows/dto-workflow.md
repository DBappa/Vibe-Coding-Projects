---
description: Goal : Create DTO's using MapStruct Mappers and refractor the code accordingly
---

# add-dtos-workflow

## Objective
Improve the project by refining requirements, creating a structured enhancement plan, generating a task list, and implementing tasks sequentially while updating progress.

---

## Steps

### 1) Requirements Refinement
- Analyze the file: `/prompts/add_dtos/requirements-draft.md`
- Inspect the project repository to understand the context.
- Improve and rewrite the draft requirements into a new file:
  - Output file: `/prompts/add-dtos/requirements.md`

---

### 2) Plan Creation
- Analyze the newly created file:
  - Input file: `/prompts/add-dtos/requirements.md`
- Create a detailed improvement plan for the project.
- Write the plan to:
  - Output file: `/prompts/add-dtos/plan.md`

---

### 3) Task List Generation
- Analyze the plan file:
  - Input file: `/prompts/add-dtos/plan.md`
- Create a **detailed enumerated task list** aligned with the plan.
- Each task item must contain a completion placeholder:
  - `[ ]` for pending
  - `[x]` for completed
- Write the task list to:
  - Output file: `/prompts/add-dtos/tasks.md`

---

### 4) Execute Tasks (Sequential Implementation)
- Inspect these files before coding:
  - `/prompts/add-dtos/requirements.md`
  - `/prompts/add-dtos/plan.md`
  - `/prompts/add-dtos/tasks.md`
- Implement tasks in the exact order listed in `tasks.md`.
- After completing each task:
  - Mark it as done using `[x]`
  - Update `/prompts/add-dtos/tasks.md` immediately (do not batch update at the end)

**Important Rules**
- Focus strictly on the tasks in `tasks.md`
- Complete tasks sequentially
- Always update progress using `[x]` as each task is completed

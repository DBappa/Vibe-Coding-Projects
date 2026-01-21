---
description: Create Beer Order Functionality with given ERD Diagram
---

# Windsurf Workflow Prompt — Beer Order (ERD → JPA Implementation)

You are working inside this repository. Follow the steps **in order**, and write outputs to the exact file paths given. Keep everything **Java-dev actionable** (code-oriented, not vague). Use **Spring Boot + JPA (Hibernate)** with **Lombok**. Prefer clean conventions and correct relationship mappings.

---

## Step 1 — ERD Analysis → Draft Requirements
1) Open and analyze the ERD image at:
- `/prompts/create-beer-order/BeerErd.png`

2) From the ERD, infer:
- Entities (tables)
- Primary keys and foreign keys
- Cardinalities (1:1, 1:M, M:N)
- Join tables (if any)
- Required vs optional relationships (nullable FKs)
- Any unique constraints implied (e.g., natural keys, composite keys)
- Any lookup/reference tables (status/type enums)

3) Create **detailed JPA implementation instructions** for a Java developer, including:
- Entity list with purpose
- For each entity:
  - fields (including IDs)
  - relationship annotations (`@OneToMany`, `@ManyToOne`, `@OneToOne`, `@ManyToMany`)
  - ownership side and `mappedBy`
  - join columns (`@JoinColumn`) and join tables (`@JoinTable`)
  - cascade strategy recommendations (where appropriate)
  - fetch type guidance (`LAZY` default, avoid `EAGER` unless needed)
  - orphan removal guidance (if child lifecycle depends on parent)
  - equals/hashCode strategy (avoid relationship fields; prefer ID-based or business key-based)
- Lombok usage:
  - recommended annotations (`@Getter/@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`)
  - warnings about `@Data` with JPA entities
  - `@ToString.Exclude` / `@EqualsAndHashCode.Exclude` for relationships

4) Write this as a draft requirements document to:
- `/prompts/create-beer-order/requirements-draft.md`

Make the draft practical: include code snippets for tricky relationships.

---

## Step 2 — Improve Requirements Draft
1) Open:
- `/prompts/create-beer-order/requirements-draft.md`

2) Inspect the project structure and existing code (if present):
- existing packages, entity patterns, base classes, conventions
- any existing Spring Boot/JPA/Lombok configurations

3) Improve and rewrite the draft into a polished final requirements document:
- clarify ambiguity
- standardize naming
- add missing constraints and edge cases
- ensure developer can implement without guessing

4) Save the improved version to:
- `/prompts/create-beer-order/requirements.md`

---

## Step 3 — Create Implementation Plan
1) Open:
- `/prompts/create-beer-order/requirements.md`

2) Produce a **detailed plan** to implement the project improvements, including:
- entity creation order (based on dependencies)
- relationship implementation order
- migrations/schema strategy (if project uses Flyway/Liquibase, follow existing convention; otherwise propose minimal approach)
- repositories + basic service/controller scaffolding (only if required by project)
- validation annotations and DTO strategy (if needed)
- testing plan: JPA tests, repository tests, and minimal smoke tests
- any serialization safeguards (Jackson: `@JsonManagedReference/@JsonBackReference` or `@JsonIgnore` where necessary)
- sample data approach (if required)

3) Write the plan to:
- `/prompts/create-beer-order/plan.md`

---

## Step 4 — Create Task List
1) Open:
- `/prompts/create-beer-order/plan.md`

2) Create a **detailed enumerated task list** aligned to the plan.
- Each task must be actionable and checkable.
- Each task must include a checkbox placeholder: `[ ]` (later `[x]` when done).
- Group tasks by phases (Entities, Repos, Services, API, Tests, Docs) if applicable.
- Include file-level specificity where possible (e.g., `Create entity Beer.java`, `Add mapping Order -> OrderLine`).

3) Write the task list to:
- `/prompts/create-beer-order/tasks.md`

---

## Step 5 — Implement Tasks and Update Checkboxes
1) Open and follow:
- `/prompts/create-beer-order/tasks.md`
- `/prompts/create-beer-order/requirements.md`
- `/prompts/create-beer-order/plan.md`

2) Implement tasks **in order**, making real code changes in the project.
3) After completing each task:
- update `/prompts/create-beer-order/tasks.md` and change `[ ]` to `[x]`
- keep tasks in sequence; do not skip ahead unless blocked (if blocked, add a note under the task)

4) Validate as you go:
- project builds successfully
- tests pass (or add tests if missing)
- JPA mappings are correct and boot starts without mapping exceptions

---

## Output Rules
- Always write to the specified files.
- Keep docs concise but complete.
- Prefer correctness over creativity.
- If the ERD has ambiguity, document assumptions explicitly in requirements.md.

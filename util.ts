import { html } from "lit-html";
import Person from "./frontend/generated/com/example/application/data/entity/Person";

console.log("A lit template looks like this: ", html`<div>hello</div>`);

const person: Person = {
  email: "hello@world.com",
  firstName: "Hello",
  lastName: "World",
  important: false,
  occupation: "General manager",
  phone: "123",
};
console.log("A person: ", person);

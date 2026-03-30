class Student {
  name: string;
  rollNo: number;
  constructor(n: string, r: number) {
    this.name = n;
    this.rollNo = r;
  }
}

let student1 = new Student("Ruhama", 123);
let student2 = new Student("Hajra", 456);

console.log(student1);
console.log(student2);

Hospitad Onboarding project
=======

## How to run tests

```bash
./run_tests.sh
```
## How to run project

```bash
./run.sh
```


### some other

##### some some


```css
.markdown {

    h1,h2,h3,h4,h5,h6 {
        border-bottom: 1px solid rgba(142, 142, 142, 0.5);
        padding-bottom: 0.75rem;
    }

    h1 {
        font-size: 2.25rem;
        margin-bottom: 2.5rem;
    }
    h2 {
        font-size: 2rem;
        margin-bottom: 2rem;
    }
    h3 {
        font-size: 1.75rem;
        margin-bottom: 2rem;
    }
    h4 {
        font-size: 1.5rem;
        margin-bottom: 1.5rem;
    }
    h5 {
        font-size: 1.25rem;
        margin-bottom: 1.5rem;
    }
    h6 {
        font-size: 1.25rem;
        margin-bottom: 1rem;
    }

    pre {
        margin-bottom: 1rem;
    }
}
```

And one more:

```ts
import {PrismaClient} from '@prisma/client'
import {withAccelerate} from '@prisma/extension-accelerate'

const prisma = new PrismaClient().$extends(withAccelerate())

const globalForPrisma = global as unknown as { prisma: typeof prisma }

if (process.env.NODE_ENV !== 'production') globalForPrisma.prisma = prisma;

export default prisma;
```


c++ 

```cpp
int main ()
{
  myfunction();
  whatever else;
} 
```

Python

```py
def myfunction(comma, separated, parameters):
  code goes here
```


Ruby

```rb
def myfunction(comma, seperated, parameters)
  code goes here
end
```

GoLang

```go
func myfunction(comma type, separated type, parameters type) type {
 code goes here
}
```

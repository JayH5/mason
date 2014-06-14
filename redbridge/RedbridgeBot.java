//defines a robot in the coopcomp simulation platform
import java.util.*;

import sim.engine.*;
import sim.field.continuous.*;
import sim.util.*;

//define steppable bot
public interface RedbridgeBot extends Steppable
{
 
 //mutation operator
 void mutate ();

 //crossover operator
 void crossover (Gene partner);

 //input into the neural network/genetic program
 //for great good of outputs?
 ArrayList<Object> input (Object...arguments);

 

}



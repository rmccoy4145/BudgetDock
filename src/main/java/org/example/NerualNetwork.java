package org.example;

public class NerualNetwork
{
    private Layer hidden1;
    private Layer hidden2;
    private Layer output;

    public NerualNetwork()
    {
        this.hidden1 = new Layer(2, 4);
        this.hidden2 = new Layer(4, 4);
        this.output = new Layer(4, 2);
    }

    public float[] predict(float[] inputs)
    {
        hidden1.forward(inputs);
        hidden1.activation();

        hidden2.forward(hidden1.nodesArray);
        hidden2.activation();

        output.forward(hidden2.nodesArray);
        output.activation();

        return output.nodesArray;
    }

    private static class Layer
    {
        float[] nodesArray;
        float[][] weightsArray;
        float[] baisArray;

        int numOfnodes;
        int numOfInputs;

        Layer(int nInputs, int numOfnodes)
        {
            this.numOfInputs = nInputs;
            this.numOfnodes = numOfnodes;

            weightsArray = new float[numOfnodes][nInputs];
            baisArray = new float[numOfnodes];
            nodesArray = new float[numOfnodes];
        }

        public void forward(float[] inputsArray)
        {
            nodesArray = new float[numOfnodes];

            for(int i = 0; i < numOfnodes; i++)
            {
                //sum of weights times inputs
                for(int j = 0; j < numOfInputs; j++)
                {
                    nodesArray[i] += weightsArray[i][j] * inputsArray[j];
                }

                //add bias
                nodesArray[i] += baisArray[i];
            }
        }

        // Relu activation function
        public void activation()
        {
            for(int i = 0; i < numOfnodes; i++)
            {
                if(nodesArray[i] < 0)
                {
                    nodesArray[i] = 0;
                }
            }
        }

    }
}

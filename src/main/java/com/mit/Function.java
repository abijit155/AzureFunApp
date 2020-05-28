package com.mit;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.QueueTrigger;
import com.microsoft.azure.functions.annotation.TableOutput;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    @FunctionName("QueueTriger")
    public void run(
            @QueueTrigger(name = "message",
                    dataType = "",
                    queueName = "bakqueue",
                    connection = "AzureWebJobsStorage")Employee employeeObj,
            @BindingName("partitionKey") String partitionKey,
            @BindingName("rowKey") String rowKey,
            @TableOutput(name = "employee",
                    tableName = "Employee",
                    partitionKey="{partitionKey}", rowKey = "{rowKey}",
                    connection = "AzureWebJobsStorage") OutputBinding<Employee> employeeTable,
            final ExecutionContext context
    ) {
        context.getLogger().info("Queue message: " + employeeObj);

        Employee outEmployee = new Employee();
        outEmployee.setPartitionKey(employeeObj.getPartitionKey());
        outEmployee.setRowKey(employeeObj.getRowKey());
        outEmployee.setEname(employeeObj.getEname());
        outEmployee.setEmail(employeeObj.getEmail());
        outEmployee.setId(employeeObj.getId());

        employeeTable.setValue(outEmployee);
        context.getLogger().info("Queue message Saved to table");
    }
}

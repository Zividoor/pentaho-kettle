package org.pentaho.di.engine.kettlenative.impl.factories;

import com.google.common.collect.ImmutableList;
import org.apache.spark.api.java.JavaSparkContext;
import org.pentaho.di.engine.api.IExecutionContext;
import org.pentaho.di.engine.api.model.IOperation;
import org.pentaho.di.engine.api.model.ITransformation;
import org.pentaho.di.engine.kettlenative.impl.IExecutableOperation;
import org.pentaho.di.engine.kettlenative.impl.SparkExecOperation;
import org.pentaho.di.trans.TransMeta;

import java.util.List;
import java.util.Optional;

import static org.pentaho.di.engine.kettlenative.impl.KettleNativeUtil.getTransMeta;

public class SparkExecOperationFactory implements IExecutableOperationFactory {

  private static List<String> supportedSteps = ImmutableList.of( "Calculator" );

  @Override public Optional<IExecutableOperation> create( IOperation operation, IExecutionContext context ) {
    ITransformation transformation = context.getTransformation();
    TransMeta transMeta = getTransMeta( transformation );
    if ( !supportedSteps.contains( transMeta.findStep( operation.getId() ).getTypeId() ) ) {
      return Optional.empty();
    }
    JavaSparkContext sparkContext = Optional.ofNullable((JavaSparkContext) context.getEnvironment().get( "sparkcontext" ))
      .orElseThrow( () -> new RuntimeException( "no executor" ));
    return Optional.of( new SparkExecOperation( operation, transformation, sparkContext ) );
  }



}
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.transform.Field

import org.apache.http.HttpEntity
import org.apache.http.HttpRequest
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.conn.scheme.PlainSocketFactory
import org.apache.http.conn.scheme.Scheme
import org.apache.http.conn.scheme.SchemeRegistry
import org.apache.http.conn.ssl.SSLSocketFactory
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.impl.conn.SingleClientConnManager

import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ec2.model.DescribeImagesRequest
import com.amazonaws.services.ec2.model.DescribeKeyPairsRequest
import com.amazonaws.services.ec2.model.DescribeSubnetsRequest
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.InstanceType
import com.amazonaws.services.ec2.model.Subnet;
import com.electriccloud.domain.FormalParameterOptionsResult

@Field
		final String USER_NAME = "userName"
@Field
		final String PASSWORD = "password"
@Field
		final String SERVICE_URL = "service_url"

@Field
		final String SECURITY_GROUP = "group"
@Field
		final String IMAGE = "image"
@Field
		final String INSTANCE_TYPE = "instanceType"
@Field
		final String KEYNAME = "keyname"
@Field
		final String AVAILABILITY_ZONE = "zone"
@Field
		final String SUBNET_ID = "subnet_id"

// Disable Amazon SDK logging
Logger.getLogger("com.amazonaws").setLevel(Level.OFF);

def result = new FormalParameterOptionsResult()

if (canGetOptions(args)) {

	def ec2 = loginEC2(args.credential[0], args.configurationParameters[SERVICE_URL])
	def list = []

	switch (args.formalParameterName) {
		case IMAGE:
			list = getImages(ec2)
			break
		case KEYNAME:
			list = getKeys(ec2)
			break
		case INSTANCE_TYPE:
			list = getInstanceTypes()
			break
		case SECURITY_GROUP:
			list = getSecurityGroups(ec2)
			break
		case SUBNET_ID:
			list = getSubnetIds(ec2, args.parameters[AVAILABILITY_ZONE])
			break
		case AVAILABILITY_ZONE:
			list = getAvailabilityZones(ec2)
			break
	}

	list.sort{ it[1] }.each {
		result.add(it[0], it[1])
	}

}

result

def loginEC2(credential, serviceURL) {
	// Disable HTTPS certificate verification
	System.setProperty("com.amazonaws.sdk.disableCertChecking", "true")

	def awsCreds = new BasicAWSCredentials(credential[USER_NAME], credential[PASSWORD])
	def clientConfig = new ClientConfiguration()

	clientConfig.setConnectionTimeout(5 * 1000)
	clientConfig.setMaxErrorRetry(1)

	def ec2 = new AmazonEC2Client(awsCreds, clientConfig)
	ec2.setEndpoint(serviceURL)

	ec2
}

boolean canGetOptions(args) {
	args?.parameters &&
			args.credential &&
			args.credential.size() > 0 &&
			args.credential[0][USER_NAME] &&
			args.credential[0][PASSWORD] &&
			args.configurationParameters[SERVICE_URL] &&
			canGetOptionsForParameter(args, args.formalParameterName)
}

boolean canGetOptionsForParameter(args, formalParameterName) {
	switch (formalParameterName) {
		case SUBNET_ID:
			return args.parameters[AVAILABILITY_ZONE]
		default:
			return true
	}
}

def getImages(AmazonEC2Client ec2) {
	def list = []

	def request = new DescribeImagesRequest()
	request.withOwners("self")

	ec2.describeImages(request).getImages().each { image ->
		list.push([
			image.getImageId(),
			"${image.getImageId()} (${image.getName()})"
		])
	}

	list
}

def getKeys(AmazonEC2Client ec2) {
	def list = []

	ec2.describeKeyPairs().getKeyPairs().each { key ->
		list.push([
			key.getKeyName() ,
			key.getKeyName()
		])
	}

	list
}

def getSubnetIds(AmazonEC2Client ec2, availabilityZone) {
	def list = []

	def request = new DescribeSubnetsRequest()
	request.withFilters([
		new Filter("availabilityZone", [availabilityZone])
	])

	ec2.describeSubnets(request).getSubnets().each { subnet ->
		list.push([
			subnet.getSubnetId(),
			"${subnet.getSubnetId()} (${subnet.cidrBlock})"
		])
	}

	list
}

def getInstanceTypes() {
	def list = []

	InstanceType.values().each { type ->
		list.push([
			type.value,
			type.value
		])
	}

	list
}

def getSecurityGroups(AmazonEC2Client ec2) {
	def list = []

	ec2.describeSecurityGroups().getSecurityGroups().each { group ->
		list.push([
			group.getGroupId(),
			"${group.getGroupId()} (${group.getGroupName()})"
		])
	}

	list
}

def getAvailabilityZones(AmazonEC2Client ec2) {
	def list = []

	ec2.describeAvailabilityZones().getAvailabilityZones().each { zone ->
		list.push([
			zone.getZoneName(),
			zone.getZoneName()
		])
	}

	list
}
